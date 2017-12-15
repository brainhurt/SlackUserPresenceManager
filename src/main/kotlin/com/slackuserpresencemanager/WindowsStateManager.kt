package com.slackuserpresencemanager

import com.slackuserpresencemanager.slack.Presence

/**
 * Created by Christian on 6/14/2017.
 * Project: SlackUserPresenceManager
 */
internal object WindowsStateManager {
    fun registerShutdownListener() {
        Runtime.getRuntime().addShutdownHook(Thread {
            HTTPManager.updatePresence(Presence.AWAY)
            HTTPManager.updateStatus(Main.getProperty("logged-off-message"), Main.getProperty("logged-off-emoji"))
        })
    }
}
