package com.slackuserpresencemanager

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.io.*
import java.util.Properties

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
object Main {

    private val LOGGER = LogManager.getLogger(Main::class.java)

    private val PROPERTY_FILE_NAME = "SlackUserPresenceManager.properties"

    private val properties = Properties()

    internal fun getProperty(key: String): String {
        return properties.getProperty(key, "")
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val file = File(PROPERTY_FILE_NAME)
        if (!file.exists()) {
            throw FileNotFoundException(PROPERTY_FILE_NAME + " was not found!")
        }
        val inputStream = FileInputStream(file)
        properties.load(inputStream)
        WindowsStateManager.registerShutdownListener()
        WindowsSessionManager()
    }
}
