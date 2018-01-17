package com.slackuserpresencemanager.listeners

import com.slackuserpresencemanager.slack.SlackApiManager
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinDef.LPARAM
import com.sun.jna.platform.win32.WinDef.LRESULT
import com.sun.jna.platform.win32.WinDef.WPARAM
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.platform.win32.WinUser.MSG
import com.sun.jna.platform.win32.WinUser.WindowProc
import com.sun.jna.platform.win32.Wtsapi32
import org.apache.logging.log4j.LogManager

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
object WindowsSessionManager : WindowProc {

    private val LOGGER = LogManager.getLogger(WindowsSessionManager::class.java)

    init {
        // define new window class
        val windowClass = "MyWindowClass"
        val hInst = Kernel32.INSTANCE.GetModuleHandle("")

        val wClass = WinUser.WNDCLASSEX()
        wClass.hInstance = hInst
        wClass.lpfnWndProc = this@WindowsSessionManager
        wClass.lpszClassName = windowClass

        // register window class
        User32.INSTANCE.RegisterClassEx(wClass)

        var lastError = Kernel32.INSTANCE.GetLastError()
        if (lastError != 0) {
            throw RuntimeException("Encountered error with code: " + lastError)
        }

        // create new window
        val hWnd = User32.INSTANCE
                .CreateWindowEx(
                        User32.WS_EX_TOPMOST,
                        windowClass,
                        "My hidden helper window, used only to catch the windows events",
                        0, 0, 0, 0, 0, null, null, hInst, null)
        lastError = Kernel32.INSTANCE.GetLastError()
        if (lastError != 0) {
            throw RuntimeException("Encountered error with code: " + lastError)
        }
        LOGGER.info("Window successfully created! Window hWnd: " + hWnd.pointer.toString())

        Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hWnd,
                Wtsapi32.NOTIFY_FOR_THIS_SESSION)

        val msg = MSG()
        while (User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) != 0) {
            User32.INSTANCE.TranslateMessage(msg)
            User32.INSTANCE.DispatchMessage(msg)
        }

        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hWnd)
        User32.INSTANCE.UnregisterClass(windowClass, hInst)
        User32.INSTANCE.DestroyWindow(hWnd)

        LOGGER.info("Program exit!")
    }

    override fun callback(hwnd: HWND, uMsg: Int, wParam: WPARAM, lParam: LPARAM): LRESULT {
        when (uMsg) {
            WinUser.WM_DESTROY -> {
                User32.INSTANCE.PostQuitMessage(0)
                return LRESULT(0)
            }
            WinUser.WM_SESSION_CHANGE -> {
                this.onSessionChange(wParam, lParam)
                return LRESULT(0)
            }
            else -> return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam)
        }
    }

    private fun onSessionChange(wParam: WPARAM, lParam: LPARAM) {
        when (wParam.toInt()) {
            Wtsapi32.WTS_CONSOLE_CONNECT -> {
                this.onConsoleConnect(lParam.toInt())
            }
            Wtsapi32.WTS_CONSOLE_DISCONNECT -> {
                this.onConsoleDisconnect(lParam.toInt())
            }
            Wtsapi32.WTS_SESSION_LOCK -> {
                this.onMachineLocked()
            }
            Wtsapi32.WTS_SESSION_UNLOCK -> {
                this.onMachineUnlocked()
            }
        }
    }

    private fun onConsoleConnect(session: Int) {
        LOGGER.info("Connecting to session: " + session)
    }

    private fun onConsoleDisconnect(session: Int) {
        LOGGER.info("Disconnecting from session: " + session)
    }

    private fun onMachineLocked() {
        SlackApiManager.isAfk = true
    }

    private fun onMachineUnlocked() {
        SlackApiManager.isAfk = false
    }
}