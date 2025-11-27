package com.vitiligo.breathe.data.mapper

import com.vitiligo.breathe.data.local.entity.LocationDetails
import com.vitiligo.breathe.data.local.relation.LocationWithDetails
import com.vitiligo.breathe.data.remote.model.AirQualityCurrentData
import com.vitiligo.breathe.data.remote.model.AirQualityForecastData
import com.vitiligo.breathe.data.remote.model.DailyWeatherForecast
import com.vitiligo.breathe.data.remote.model.HourlyWeatherForecast
import com.vitiligo.breathe.data.remote.model.LocationAirQualityData
import com.vitiligo.breathe.data.remote.model.LocationClimateDetailsResponse
import com.vitiligo.breathe.data.remote.model.LocationWeatherData
import com.vitiligo.breathe.domain.model.Pollutant
import com.vitiligo.breathe.domain.model.ui.AqiCardData
import com.vitiligo.breathe.domain.model.ui.ForecastDayDataExtended
import com.vitiligo.breathe.domain.model.ui.HourlyForecastData
import com.vitiligo.breathe.domain.model.ui.LocationDetailsData
import com.vitiligo.breathe.domain.util.getAqiCategory
import com.vitiligo.breathe.domain.util.getPollutantDetails
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocationClimateDetailsResponse.toDetailsEntity(locationId: Int): LocationDetails {
    return LocationDetails(
        locationId = locationId,
        lastUpdatedTimestamp = System.currentTimeMillis(),
        weatherData = this.weather ?: LocationWeatherData(),
        airQualityData = this.airQuality ?: LocationAirQualityData()
    )
}

fun LocationWithDetails.toDomainModel(): LocationDetailsData? {
    val details = this.details ?: return null
    val location = this.location
    val weather = details.weatherData
    val aqi = details.airQualityData
    val offset = location.utcOffsetSeconds

    val localTime = Instant.now()
        .atZone(ZoneOffset.ofTotalSeconds(offset))
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    val pollutants = mapPollutants(aqi.current)

    val dominant = "PM2.5"
    val dominantVal = aqi.current?.pm2_5 ?: 0.0

    val currentAqiVal = aqi.current?.aqi ?: 0
    val aqiCard = AqiCardData(
        aqi = currentAqiVal,
        category = getAqiCategory(currentAqiVal),
        dominantPollutant = dominant,
        dominantPollutantValue = dominantVal,
        tempC = weather.current?.temperature ?: 0.0,
        weatherCode = weather.current?.weatherCode ?: 0,
        windSpeedKph = weather.current?.windSpeed ?: 0.0,
        windSpeedDeg = weather.current?.windDirection?.toDouble() ?: 0.0,
        humidityPercent = weather.current?.humidity ?: 0.0
    )

    val hourly = mapHourlyForecast(weather.hourly, aqi.hourly, offset)
    val daily = mapDailyForecast(weather.daily, aqi.daily, offset)

    return LocationDetailsData(
        locationName = location.name,
        localTime = localTime,
        aqiCardData = aqiCard,
        hourlyForecasts = hourly,
        dailyForecasts = daily,
        pollutants = pollutants
    )
}

private fun mapPollutants(current: AirQualityCurrentData?): List<Pollutant> {
    val list = mutableListOf<Pollutant>()

    list.add(getPollutantDetails("pm2_5", current?.pm2_5 ?: 0.0))
    list.add(getPollutantDetails("pm10", current?.pm10 ?: 0.0))
    list.add(getPollutantDetails("o3", current?.o3 ?: 0.0))
    list.add(getPollutantDetails("no2", current?.no2 ?: 0.0))
    list.add(getPollutantDetails("so2", current?.so2 ?: 0.0))
    list.add(getPollutantDetails("co", current?.co ?: 0.0))

    return list
}

private fun mapHourlyForecast(
    wHourly: HourlyWeatherForecast?,
    aHourly: AirQualityForecastData?,
    offset: Int
): List<HourlyForecastData> {
    val times = wHourly?.time ?: return emptyList()
    val size = minOf(
        times.size,
        wHourly.temperature?.size ?: 0,
        aHourly?.aqi?.size ?: 0
    )

    val result = mutableListOf<HourlyForecastData>()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    for (i in 0 until size) {
        val timeLabel = Instant.ofEpochSecond(times[i])
            .atZone(ZoneOffset.ofTotalSeconds(offset))
            .format(formatter)

        val aqiVal = aHourly?.aqi?.getOrNull(i) ?: 0

        result.add(HourlyForecastData(
            timeLabel = timeLabel,
            aqi = aqiVal,
            aqiCategory = getAqiCategory(aqiVal),
            weatherCode = wHourly.weatherCode?.getOrNull(i) ?: 0,
            tempC = wHourly.temperature?.getOrNull(i) ?: 0.0,
            windSpeedKph = wHourly.windSpeed?.getOrNull(i) ?: 0.0,
            windSpeedDeg = wHourly.windDirection?.getOrNull(i)?.toDouble() ?: 0.0,
            humidityPercent = wHourly.humidity?.getOrNull(i) ?: 0.0
        ))
    }

    return result
}

private fun mapDailyForecast(
    wDaily: DailyWeatherForecast?,
    aDaily: AirQualityForecastData?,
    offset: Int
): List<ForecastDayDataExtended> {
    val times = wDaily?.time ?: return emptyList()
    val size = minOf(times.size, aDaily?.aqi?.size ?: 0)

    val result = mutableListOf<ForecastDayDataExtended>()
    val formatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())

    for (i in 0 until size) {
        val dayLabel = Instant.ofEpochSecond(times[i])
            .atZone(ZoneOffset.ofTotalSeconds(offset))
            .format(formatter)

        val aqiVal = aDaily?.aqi?.getOrNull(i) ?: 0

        result.add(ForecastDayDataExtended(
            dayLabel = dayLabel,
            aqi = aqiVal,
            aqiCategory = getAqiCategory(aqiVal),
            weatherCode = wDaily.weatherCode?.getOrNull(i) ?: 0,
            highTempC = wDaily.temperatureMax?.getOrNull(i) ?: 0.0,
            lowTempC = wDaily.temperatureMin?.getOrNull(i) ?: 0.0,
            windSpeedKph = wDaily.windSpeed?.getOrNull(i) ?: 0.0,
            windSpeedDeg = wDaily.windDirection?.getOrNull(i)?.toDouble() ?: 0.0,
            humidityPercent = wDaily.humidity?.getOrNull(i) ?: 0.0
        ))
    }

    return result
}