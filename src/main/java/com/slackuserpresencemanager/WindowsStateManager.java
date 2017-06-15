package com.slackuserpresencemanager;

import com.slackuserpresencemanager.slack.Presence;

/**
 * Created by Christian on 6/14/2017.
 * Project: SlackUserPresenceManager
 */
class WindowsStateManager {
    static void registerShutdownListener() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            HTTPManager.updatePresence(Presence.AWAY);
            HTTPManager.updateStatus(Main.getProperty("logged-off-message"), Main.getProperty("logged-off-emoji"));
        }));
    }
}
