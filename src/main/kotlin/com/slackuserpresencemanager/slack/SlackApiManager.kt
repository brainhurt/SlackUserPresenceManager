package com.slackuserpresencemanager.slack

import com.google.gson.Gson
import com.slackuserpresencemanager.Main
import org.apache.http.client.fluent.Request
import org.apache.http.client.utils.URIBuilder
import org.apache.logging.log4j.LogManager
import java.util.HashMap

/**
 * Created by Tharaka on 6/12/2017.
 * For project: SlackUserPresenceManager
 */
object SlackApiManager {

    private val LOGGER = LogManager.getLogger(SlackApiManager::class.java)

    private val BASE_URL = "https://slack.com/api/"

    private val RESOURCE_STATUS = "users.profile.set"

    private val RESOURCE_PRESENCE = "users.setPresence"

    private fun update(resource: String, key: String, value: String) {
        for (token in Main.getProperty("tokens").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val uriBuilder = URIBuilder(BASE_URL + resource)
            uriBuilder.addParameter("token", token)
            uriBuilder.addParameter(key, value)
            for (i in 1..3) {
                try {
                    println("attempt $i")
                    Request.Get(uriBuilder.build())
                            .connectTimeout(5000)
                            .socketTimeout(5000)
                            .execute()
                    break
                } catch (e: Exception) {
                    LOGGER.error("Caught error ${e.localizedMessage}, but moving onwards... [Retry attempt = $i]", e)
                    if (i != 3) {
                        Thread.sleep(10000)
                    }
                }
            }
        }
    }

    fun updateStatus(message: String, emoji: String) {
        val statusInfo = HashMap<String, String>()
        statusInfo.put("status_text", message)
        statusInfo.put("status_emoji", ":$emoji:")
        update(RESOURCE_STATUS, "profile", Gson().toJson(statusInfo))
    }

    fun updatePresence(presence: Presence) {
        update(RESOURCE_PRESENCE, "presence", presence.presenceString)
    }
}
