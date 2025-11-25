package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class ForecastDayData(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherCode: Int,
    val highTempC: Int,
    val lowTempC: Int
)

data class ForecastDayDataExtended(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherCode: Int,
    val highTempC: Double,
    val lowTempC: Double,
    val windSpeedKph: Double,
    val windSpeedDeg: Double,
    val humidityPercent: Double,
)
