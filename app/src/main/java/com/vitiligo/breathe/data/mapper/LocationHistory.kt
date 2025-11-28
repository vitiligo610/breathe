package com.vitiligo.breathe.data.mapper

import com.vitiligo.breathe.data.local.entity.LocationHistory
import com.vitiligo.breathe.data.remote.model.HistoryForecast
import com.vitiligo.breathe.data.remote.model.LocationAirQualityHistoryResponse
import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.util.getPollutantCategory
import java.time.Instant
import java.time.ZoneOffset

fun LocationAirQualityHistoryResponse.toHistoryEntity(locationId: Int): LocationHistory {
    return LocationHistory(
        locationId = locationId,
        lastUpdatedTimestamp = System.currentTimeMillis(),
        hourlyForecast = this.hourly ?: HistoryForecast(),
        dailyForecast = this.daily ?: HistoryForecast()
    )
}

fun HistoryForecast.getAvailableTabs(): List<HistoryTabOption> {
    val tabs = mutableListOf<HistoryTabOption>()

    if (!aqi.isNullOrEmpty()) tabs.add(HistoryTabOption.AQI)
    if (!pm2_5.isNullOrEmpty()) tabs.add(HistoryTabOption.PM2_5)
    if (!pm10.isNullOrEmpty()) tabs.add(HistoryTabOption.PM10)
    if (!o3.isNullOrEmpty()) tabs.add(HistoryTabOption.O3)
    if (!no2.isNullOrEmpty()) tabs.add(HistoryTabOption.NO2)
    if (!so2.isNullOrEmpty()) tabs.add(HistoryTabOption.SO2)
    if (!co.isNullOrEmpty()) tabs.add(HistoryTabOption.CO)

    if (tabs.isEmpty()) tabs.add(HistoryTabOption.AQI)

    return tabs
}

fun HistoryForecast.extractPoints(
    option: HistoryTabOption,
    utcOffsetSeconds: Int
): List<HistoryPoint> {
    val times = this.time ?: return emptyList()
    val values: List<Double> = when (option) {
        HistoryTabOption.AQI -> this.aqi?.filterNotNull()?.map { it.toDouble() }
        HistoryTabOption.PM2_5 -> this.pm2_5?.filterNotNull()
        HistoryTabOption.PM10 -> this.pm10?.filterNotNull()
        HistoryTabOption.O3 -> this.o3?.filterNotNull()
        HistoryTabOption.NO2 -> this.no2?.filterNotNull()
        HistoryTabOption.SO2 -> this.so2?.filterNotNull()
        HistoryTabOption.CO -> this.co?.filterNotNull()
    } ?: return emptyList()

    val size = minOf(times.size, values.size)
    val result = ArrayList<HistoryPoint>(size)

    for (i in 0 until size) {
        val dateTime = Instant.ofEpochSecond(times[i])
            .atZone(ZoneOffset.ofTotalSeconds(utcOffsetSeconds))
            .toLocalDateTime()

        val aqiCategory = getPollutantCategory(values[i], option.key)
        result.add(HistoryPoint(dateTime, values[i], aqiCategory))
    }
    return result
}