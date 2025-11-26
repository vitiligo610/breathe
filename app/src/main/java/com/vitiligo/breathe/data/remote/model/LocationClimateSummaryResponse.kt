package com.vitiligo.breathe.data.remote.model

import com.google.gson.annotations.SerializedName

class LocationClimateSummaryResponse : BaseLocationResponse() {

    @SerializedName("units")
    val units: ClimateSummaryUnits? = null

    @SerializedName("current")
    val current: CurrentData? = null

    @SerializedName("forecast")
    val forecast: ForecastData? = null
}

data class ClimateSummaryUnits(
    val time: String? = null,
    val temperature: String? = null,
    @SerializedName("weather_code") val weatherCode: String? = null,
    @SerializedName("temperature_max") val temperatureMax: String? = null,
    @SerializedName("temperature_min") val temperatureMin: String? = null,
    val aqi: String? = null
)

data class CurrentData(
    val time: Long? = null,
    val temperature: Double? = null,
    @SerializedName("weather_code") val weatherCode: Int? = null,
    val aqi: Int? = null
)

data class ForecastData(
    val time: List<Long>? = null,
    @SerializedName("temperature_max") val temperatureMax: List<Double>? = null,
    @SerializedName("temperature_min") val temperatureMin: List<Double>? = null,
    @SerializedName("weather_code") val weatherCode: List<Int>? = null,
    val aqi: List<Int>? = null
)
