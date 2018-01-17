package com.slackuserpresencemanager.slack

import com.google.gson.Gson
import com.slackuserpresencemanager.Main
import org.apache.http.client.fluent.Request
import org.apache.http.client.utils.URIBuilder
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.net.URISyntaxException
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

    var isAfk: Boolean = false
        set(value) {
            field = value
            set()
        }

    var isInMeeeting = false
        set(value) {
            field = value
            set()
        }

    var meetingSubject = ""

    var isLoggedOff = false
        set(value) {
            field = value
            set()
        }

    private fun update(resource: String, key: String, value: String) {
        try {
            for (token in Main.getProperty("tokens").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                val uriBuilder = URIBuilder(BASE_URL + resource)
                uriBuilder.addParameter("token", token)
                uriBuilder.addParameter(key, value)
                Request.Get(uriBuilder.build())
                        .connectTimeout(5000)
                        .socketTimeout(5000)
                        .execute()
            }
        } catch (e: URISyntaxException) {
            LOGGER.error(e.message, e)
        } catch (e: IOException) {
            LOGGER.error(e.message, e)
        }
    }

    private fun updateStatus(message: String, emoji: String) {
        val statusInfo = HashMap<String, String>()
        statusInfo.put("status_text", message)
        statusInfo.put("status_emoji", ":$emoji:")
        update(RESOURCE_STATUS, "profile", Gson().toJson(statusInfo))
    }

    private fun updatePresence(presence: Presence) {
        update(RESOURCE_PRESENCE, "presence", presence.presenceString)
    }

    private fun setAFK() {
        updateStatus(Main.getProperty("active-message"), Main.getProperty("active-emoji"))
        updatePresence(Presence.AWAY)
    }

    private fun setAvailable() {
        updateStatus(Main.getProperty("active-message"), Main.getProperty("active-emoji"))
        updatePresence(Presence.AUTO)
    }

    private fun setInMeeting() {
        updateStatus(Main.getProperty("meeting-message").replace("###MEETING_SUBJECT###", meetingSubject), Main.getProperty("meeting-emoji"))
        updatePresence(Presence.AWAY)
    }

    private fun setLoggedOff() {
        updatePresence(Presence.AWAY)
        updateStatus(Main.getProperty("logged-off-message"), Main.getProperty("logged-off-emoji"))
    }

    private fun set() {
        when {
            isLoggedOff -> setLoggedOff()
            isAfk -> setAFK()
            isInMeeeting -> setInMeeting()
            else -> setAvailable()
        }
    }
}
