package com.slackuserpresencemanager;

import com.google.gson.Gson;
import com.slackuserpresencemanager.slack.Presence;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tharaka on 6/12/2017.
 * For project: SlackUserPresenceManager
 */
public class HTTPManager {

    private final static Logger LOGGER = LogManager.getLogger(HTTPManager.class);

    private final static String BASE_URL = "https://slack.com/api/";

    private final static String RESOURCE_STATUS = "users.profile.set";

    private final static String RESOURCE_PRESENCE = "users.setPresence";

    private static void update(final String resource, final String key, final String value) {
        try {
            for (String token : Main.getProperty("tokens").split(",")) {
                URIBuilder uriBuilder = new URIBuilder(BASE_URL.concat(resource));
                uriBuilder.addParameter("token", token);
                uriBuilder.addParameter(key, value);
                Request.Get(uriBuilder.build())
                        .connectTimeout(1000)
                        .socketTimeout(1000)
                        .execute();
            }
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void updateStatus(final String message, final String emoji) {
        Map<String, String> statusInfo = new HashMap<>();
        statusInfo.put("status_text", message);
        statusInfo.put("status_emoji", ":".concat(emoji).concat(":"));
        update(RESOURCE_STATUS, "profile", new Gson().toJson(statusInfo));
    }

    public static void updatePresence(final Presence presence) {
        update(RESOURCE_PRESENCE, "presence", presence.getPresenceString());
    }
}
