package com.slackuserpresencemanager;

import com.google.gson.Gson;
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
class HTTPManager {

    private final static Logger LOGGER = LogManager.getLogger(HTTPManager.class);

    private final static String BASE_URL = "https://slack.com/api/";

    private final static String RESOURCE_STATUS = "users.profile.set";

    private final static String RESOURCE_PRESENCE = "users.setPresence";

    private static void update(final String resource, final String key, final String value) {
        try {
            URIBuilder uriBuilder = new URIBuilder(BASE_URL.concat(resource));
            uriBuilder.addParameter("token", Main.getProperty("token"));
            uriBuilder.addParameter(key, value);
            Request.Get(uriBuilder.build())
                    .connectTimeout(1000)
                    .socketTimeout(1000)
                    .execute();
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static void updateStatus(final String message, final String emoji) {
        Map<String, String> statusInfo = new HashMap<>();
        statusInfo.put("status_text", message);
        statusInfo.put("status_emoji", emoji);
        update(RESOURCE_STATUS, "profile", new Gson().toJson(statusInfo));
    }

    static void updatePresence(final String presence) {
        update(RESOURCE_PRESENCE, "presence", presence);
    }
}
