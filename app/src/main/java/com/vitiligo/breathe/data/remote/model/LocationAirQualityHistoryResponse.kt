package com.vitiligo.breathe.data.remote.model

import com.google.gson.annotations.SerializedName

class LocationAirQualityHistoryResponse : BaseLocationResponse() {
    @SerializedName("units")
    val units: AirQualityUnits? = null

    @SerializedName("hourly")
    val hourly: HistoryForecast? = null

    @SerializedName("daily")
    val daily: HistoryForecast? = null
}

data class HistoryForecast(
    val time: List<Long>? = null,
    val aqi: List<Int?>? = null,
    @SerializedName("pm2_5") val pm2_5: List<Double?>? = null,
    @SerializedName("pm10") val pm10: List<Double?>? = null,
    val o3: List<Double?>? = null,
    val co: List<Double?>? = null,
    val no2: List<Double?>? = null,
    val so2: List<Double?>? = null
)