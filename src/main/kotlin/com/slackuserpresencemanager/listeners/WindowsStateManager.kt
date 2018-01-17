package com.slackuserpresencemanager.listeners

import com.slackuserpresencemanager.slack.SlackApiManager

/**
 * Created by Christian on 6/14/2017.
 * Project: SlackUserPresenceManager
 */
object WindowsStateManager {

    init {
        registerShutdownListener()
    }

    private fun registerShutdownListener() {
        Runtime.getRuntime().addShutdownHook(Thread {
            SlackApiManager.isLoggedOff = true
        })
    }
}
