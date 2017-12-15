package com.slackuserpresencemanager.slack

/**
 * Created by Christian on 6/14/2017.
 * Project: SlackUserPresenceManager
 */
enum class Presence private constructor(val presenceString: String) {
    AUTO("auto"),
    AWAY("away")
}
