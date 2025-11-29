package com.vitiligo.breathe.data.mapper

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class LocalDateTimeMapper(offset: Int) {

    private val zoneOffset: ZoneOffset = ZoneOffset.ofTotalSeconds(offset)
    private val now: ZonedDateTime = ZonedDateTime.now(zoneOffset)
    private val currentDay = now.toLocalDate()

    private val hourlyFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dailyFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())

    fun mapHourlyLabel(epochSecond: Long): String {
        val forecastZonedDateTime = Instant.ofEpochSecond(epochSecond).atZone(zoneOffset)

        val forecastDay = forecastZonedDateTime.toLocalDate()
        val forecastHour = forecastZonedDateTime.hour

        return if (
            forecastDay == currentDay &&
            forecastHour == now.hour
        ) {
            "Now"
        } else {
            forecastZonedDateTime.format(hourlyFormatter)
        }
    }

    fun mapDailyLabel(epochSecond: Long): String {
        val forecastDate = Instant.ofEpochSecond(epochSecond).atZone(zoneOffset).toLocalDate()

        return if (forecastDate == currentDay) {
            "Today"
        } else {
            forecastDate.format(dailyFormatter)
        }
    }
}