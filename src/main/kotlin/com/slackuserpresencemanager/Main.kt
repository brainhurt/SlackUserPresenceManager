package com.slackuserpresencemanager

import com.slackuserpresencemanager.exchange.ExchangeManager
import com.slackuserpresencemanager.slack.SlackApiManager
import org.apache.logging.log4j.LogManager
import java.util.Properties


/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
object Main {

    private val LOGGER = LogManager.getLogger(Main::class.java)

    private val PROPERTY_FILE_NAME = "SlackUserPresenceManager.properties"

    private val properties = Properties()

    fun getProperty(key: String): String {
        return properties.getProperty(key, "")
    }

    init {
//        val file = File(PROPERTY_FILE_NAME)
//        if (!file.exists()) {
//            throw FileNotFoundException("File '$PROPERTY_FILE_NAME' was not found!")
//        }
//        val inputStream = FileInputStream(file)
        val inputStream = ClassLoader.getSystemClassLoader().getResource(PROPERTY_FILE_NAME).openStream()
        properties.load(inputStream)
//        WindowsStateManager
//        WindowsSessionManager
        ExchangeManager
        SlackApiManager.isLoggedOff = false // this is just to trigger SlackApiManager.set() method.
    }
}

fun main(args: Array<String>) {
    Main
}