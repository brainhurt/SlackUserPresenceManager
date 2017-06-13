package com.slackuserpresencemanager;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import com.sun.jna.platform.win32.Wtsapi32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
public class WindowsSessionManager implements WindowProc {

    private static final Logger LOGGER = LogManager.getLogger(WindowsSessionManager.class);

    WindowsSessionManager() {
        // define new window class
        String windowClass = "MyWindowClass";
        HMODULE hInst = Kernel32.INSTANCE.GetModuleHandle("");

        // create new window
        HWND hWnd = User32.INSTANCE
                .CreateWindowEx(
                        User32.WS_EX_TOPMOST,
                        windowClass,
                        "My hidden helper window, used only to catch the windows events",
                        0, 0, 0, 0, 0,
                        null, null, hInst, null);
        int lastError = Kernel32.INSTANCE.GetLastError();
        if (lastError != 0) {
            throw new RuntimeException("Encountered error with code: " + lastError);
        }
        LOGGER.info("Window successfully created! Window hWnd: " + hWnd.getPointer().toString());

        Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hWnd,
                Wtsapi32.NOTIFY_FOR_THIS_SESSION);

        MSG msg = new MSG();
        while (User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) != 0) {
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
        }

        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hWnd);
        User32.INSTANCE.UnregisterClass(windowClass, hInst);
        User32.INSTANCE.DestroyWindow(hWnd);

        LOGGER.info("Program exit!");
    }

    @Override
    public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam) {
        switch (uMsg) {
            case WinUser.WM_DESTROY: {
                User32.INSTANCE.PostQuitMessage(0);
                return new LRESULT(0);
            }
            case WinUser.WM_SESSION_CHANGE: {
                this.onSessionChange(wParam, lParam);
                return new LRESULT(0);
            }
            default:
                return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
        }
    }

    private void onSessionChange(WPARAM wParam, LPARAM lParam) {
        switch (wParam.intValue()) {
            case Wtsapi32.WTS_CONSOLE_CONNECT: {
                this.onConsoleConnect(lParam.intValue());
                break;
            }
            case Wtsapi32.WTS_CONSOLE_DISCONNECT: {
                this.onConsoleDisconnect(lParam.intValue());
                break;
            }
            case Wtsapi32.WTS_SESSION_LOGON: {
                this.onMachineLogon();
                break;
            }
            case Wtsapi32.WTS_SESSION_LOGOFF: {
                this.onMachineLogoff();
                break;
            }
            case Wtsapi32.WTS_SESSION_LOCK: {
                this.onMachineLocked();
                break;
            }
            case Wtsapi32.WTS_SESSION_UNLOCK: {
                this.onMachineUnlocked();
                break;
            }
        }
    }

    private void onConsoleConnect(final int session) {
        LOGGER.info("Connecting to session: " + session);
    }

    private void onConsoleDisconnect(final int session) {
        LOGGER.info("Disconnecting from session: " + session);
    }

    private void onMachineLocked() {
        HTTPManager.updateStatus(Main.getProperty("away-message"), Main.getProperty("away-emoji"));
        HTTPManager.updatePresence("away");
    }

    private void onMachineUnlocked() {
        HTTPManager.updateStatus(Main.getProperty("active-message"), Main.getProperty("active-emoji"));
        HTTPManager.updatePresence("auto");
    }

    private void onMachineLogoff() {
        HTTPManager.updateStatus(Main.getProperty("computer-shutdown-message"), Main.getProperty("computer-shutdown-emoji"));
        HTTPManager.updatePresence("away");
    }

    private void onMachineLogon() {
        HTTPManager.updateStatus(Main.getProperty("active-message"), Main.getProperty("active-emoji"));
        HTTPManager.updatePresence("auto");
    }
}