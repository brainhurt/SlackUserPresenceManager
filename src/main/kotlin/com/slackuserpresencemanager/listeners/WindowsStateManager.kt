package com.slackuserpresencemanager.listeners

import com.slackuserpresencemanager.Main
import com.slackuserpresencemanager.slack.Presence
import com.slackuserpresencemanager.slack.SlackApiManager

/**
 * Created by Christian on 6/14/2017.
 * Project: SlackUserPresenceManager
 */
class WindowsStateManager {

    init {
        registerShutdownListener()
    }

    private fun registerShutdownListener() {
        Runtime.getRuntime().addShutdownHook(Thread {
            SlackApiManager.updatePresence(Presence.AWAY)
            SlackApiManager.updateStatus(Main.getProperty("logged-off-message"), Main.getProperty("logged-off-emoji"))
        })
    }
}
