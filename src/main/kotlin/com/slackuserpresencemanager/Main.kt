package com.slackuserpresencemanager

import com.slackuserpresencemanager.listeners.WindowsSessionManager
import com.slackuserpresencemanager.listeners.WindowsStateManager
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
class Main {

    init {
        val file = File(PROPERTY_FILE_NAME)
        if (!file.exists()) {
            throw FileNotFoundException("File '" + PROPERTY_FILE_NAME + "' was not found!")
        }
        val inputStream = FileInputStream(file)
        properties.load(inputStream)
        WindowsStateManager()
        WindowsSessionManager()
    }

    companion object {
        private val LOGGER = LogManager.getLogger(Main::class.java)

        private val PROPERTY_FILE_NAME = "SlackUserPresenceManager.properties"

        private val properties = Properties()

        fun getProperty(key: String): String {
            return properties.getProperty(key, "")
        }
    }
}

fun main(args: Array<String>) {
    Main()
}
