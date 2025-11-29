package com.vitiligo.breathe.data.mapper

import com.vitiligo.breathe.data.local.entity.DailyForecast
import com.vitiligo.breathe.data.local.entity.LocationSummary
import com.vitiligo.breathe.data.local.entity.UserLocation
import com.vitiligo.breathe.data.local.relation.LocationWithSummary
import com.vitiligo.breathe.data.remote.model.BaseLocationResponse
import com.vitiligo.breathe.data.remote.model.LocationClimateSummaryResponse
import com.vitiligo.breathe.domain.model.ui.ForecastDayData
import com.vitiligo.breathe.domain.model.ui.LocationCardData
import com.vitiligo.breathe.domain.util.getAqiCategory
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

fun BaseLocationResponse.toUserLocation(): UserLocation {
    return UserLocation(
        name = name ?: "Unknown",
        country = country ?: "",
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        timezone = timezone ?: "UTC",
        utcOffsetSeconds = utcOffsetSeconds ?: 0
    )
}

fun LocationClimateSummaryResponse.toSummaryEntity(parentId: Int): LocationSummary {

    return LocationSummary(
        locationId = parentId,
        lastUpdatedTimestamp = System.currentTimeMillis(),
        currentTemp = current?.temperature ?: 0.0,
        currentAqi = current?.aqi ?: 0,
        currentWeatherCode = current?.weatherCode ?: 0,
        dailyForecasts = mapForecastsToEntityData(
            this.forecast?.time,
            this.forecast?.aqi,
            this.forecast?.weatherCode,
            this.forecast?.temperatureMax,
            this.forecast?.temperatureMin,
            this.utcOffsetSeconds ?: 0
        )
    )
}

fun LocationWithSummary.toDomainModel(): LocationCardData {
    val loc = this.location
    val sum = this.summary

    val domainForecasts = sum?.dailyForecasts?.map {
        ForecastDayData(
            dayLabel = it.dayLabel,
            aqi = it.aqi,
            aqiCategory = it.aqiCategory,
            weatherCode = it.weatherCode,
            highTempC = it.highTempC,
            lowTempC = it.lowTempC
        )
    } ?: emptyList()

    val localTime = Instant.now()
        .atZone(ZoneOffset.ofTotalSeconds(loc.utcOffsetSeconds))
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    val currentAqi = sum?.currentAqi ?: 0
    val category = getAqiCategory(currentAqi)

    return LocationCardData(
        id = loc.id,
        city = loc.name,
        subtitle = loc.country,
        currentAqi = currentAqi,
        currentAqiCategory = category,
        currentTempC = sum?.currentTemp?.roundToInt() ?: 0,
        currentWeatherCode = sum?.currentWeatherCode ?: 0,
        forecasts = domainForecasts,
        time = localTime,
        utcOffsetSeconds = loc.utcOffsetSeconds
    )
}

private fun mapForecastsToEntityData(
    times: List<Long>?,
    aqis: List<Int>?,
    codes: List<Int>?,
    maxTemps: List<Double>?,
    minTemps: List<Double>?,
    offsetSeconds: Int
): List<DailyForecast> {
    if (times == null || aqis == null) return emptyList()

    val result = mutableListOf<DailyForecast>()

    val size = minOf(times.size, aqis.size, codes?.size ?: 0, maxTemps?.size ?: 0, minTemps?.size ?: 0)
    val dateTimeMapper = LocalDateTimeMapper(offsetSeconds)

    for (i in 0 until size) {
        result.add(
            DailyForecast(
                dayLabel = dateTimeMapper.mapDailyLabel(times[i]),
                aqi = aqis[i],
                aqiCategory = getAqiCategory(aqis[i]),
                weatherCode = codes?.get(i) ?: 0,
                highTempC = maxTemps?.get(i)?.roundToInt() ?: 0,
                lowTempC = minTemps?.get(i)?.roundToInt() ?: 0
            )
        )
    }
    return result
}