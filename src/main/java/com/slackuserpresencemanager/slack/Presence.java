package com.slackuserpresencemanager.slack;

/**
 * Created by Christian on 6/14/2017.
 * Project: SlackUserPresenceManager
 */
public enum Presence {
    AUTO("auto"),
    AWAY("away");

    private String slackString;

    Presence(final String slackString) {
        this.slackString = slackString;
    }

    public String getPresenceString() {
        return this.slackString;
    }
}
