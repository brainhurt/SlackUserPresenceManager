package com.slackuserpresencemanager.exchange

import com.slackuserpresencemanager.Main
import com.slackuserpresencemanager.slack.SlackApiManager
import microsoft.exchange.webservices.data.core.ExchangeService
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder
import microsoft.exchange.webservices.data.core.service.item.Appointment
import microsoft.exchange.webservices.data.credential.WebCredentials
import microsoft.exchange.webservices.data.search.CalendarView
import org.apache.commons.lang3.time.DateUtils
import java.net.URI
import java.util.Date


/**
 * @author Tharaka De Silva (tharaka.uo@gmail.com)
 * 1/16/2018 6:02 PM
 */
object ExchangeManager {

    private val service = ExchangeService(ExchangeVersion.Exchange2010_SP2)

    init {
        service.url = URI(Main.getProperty("exchange-server"))
        service.credentials = WebCredentials(Main.getProperty("exchange-username"), Main.getProperty("exchange-password"))
        Thread {
            while (true) {
                val appointment = findNextAppointment() ?: break // if you don't have an appointment for the next 5 days, you probably don't need this module.
                SlackApiManager.meetingSubject = appointment.subject
                val currentTime = Date()
                val startTime = appointment.start
                val endTime = appointment.end
                if (currentTime < startTime) {
                    Thread.sleep(startTime.time - currentTime.time)
                } else if (currentTime > startTime && currentTime < endTime) {
                    SlackApiManager.isInMeeeting = true
                    Thread.sleep(endTime.time - currentTime.time)
                }
                SlackApiManager.isInMeeeting = false
            }
        }.start()
    }

    private fun findNextAppointment(): Appointment? {
        val startDate = Date()
        val endDate = DateUtils.addDays(startDate, 5)
        val calendarFolder = CalendarFolder.bind(service, WellKnownFolderName.Calendar)
        val results = calendarFolder.findAppointments(CalendarView(startDate, endDate, 1))
        if (results.items.size > 0) {
            return results.items[0]
        }
        return null
    }
}