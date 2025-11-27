package com.vitiligo.breathe.data.remote.model

import com.google.gson.annotations.SerializedName

class LocationClimateDetailsResponse : BaseLocationResponse() {

    @SerializedName("weather")
    val weather: LocationWeatherData? = null

    @SerializedName("air_quality")
    val airQuality: LocationAirQualityData? = null
}

data class LocationWeatherData(
    @SerializedName("units") val units: WeatherUnits? = null,
    @SerializedName("current") val current: CurrentWeatherData? = null,
    @SerializedName("hourly") val hourly: HourlyWeatherForecast? = null,
    @SerializedName("daily") val daily: DailyWeatherForecast? = null
)

data class WeatherUnits(
    val temperature: String? = null,
    val humidity: String? = null,
    @SerializedName("weather_code") val weatherCode: String? = null,
    @SerializedName("wind_speed") val windSpeed: String? = null,
    @SerializedName("wind_direction") val windDirection: String? = null
)

data class CurrentWeatherData(
    val temperature: Double? = null,
    val humidity: Double? = null,
    @SerializedName("weather_code") val weatherCode: Int? = null,
    @SerializedName("wind_speed") val windSpeed: Double? = null,
    @SerializedName("wind_direction") val windDirection: Int? = null
)

open class FutureForecast {
    val time: List<Long>? = null
    val humidity: List<Double>? = null
    @SerializedName("weather_code") val weatherCode: List<Int>? = null
    @SerializedName("wind_speed") val windSpeed: List<Double>? = null
    @SerializedName("wind_direction") val windDirection: List<Int>? = null
}

class HourlyWeatherForecast : FutureForecast() {
    val temperature: List<Double>? = null
}

class DailyWeatherForecast : FutureForecast() {
    @SerializedName("temperature_max") val temperatureMax: List<Double>? = null
    @SerializedName("temperature_min") val temperatureMin: List<Double>? = null
}

data class LocationAirQualityData(
    @SerializedName("units") val units: AirQualityUnits? = null,
    @SerializedName("current") val current: AirQualityCurrentData? = null,
    @SerializedName("hourly") val hourly: AirQualityForecastData? = null,
    @SerializedName("daily") val daily: AirQualityForecastData? = null
)

data class AirQualityUnits(
    val aqi: String? = null,
    val pm2_5: String? = null,
    val pm10: String? = null,
    val o3: String? = null,
    val co: String? = null,
    val no2: String? = null,
    val so2: String? = null
)

data class AirQualityCurrentData(
    val aqi: Int? = null,
    val pm2_5: Double? = null,
    val pm10: Double? = null,
    val o3: Double? = null,
    val co: Double? = null,
    val no2: Double? = null,
    val so2: Double? = null
)

data class AirQualityForecastData(
    val time: List<Long>? = null,
    val aqi: List<Int>? = null,
    val pm2_5: List<Double>? = null,
    val pm10: List<Double>? = null,
    val o3: List<Double>? = null,
    val co: List<Double>? = null,
    val no2: List<Double>? = null,
    val so2: List<Double>? = null
)