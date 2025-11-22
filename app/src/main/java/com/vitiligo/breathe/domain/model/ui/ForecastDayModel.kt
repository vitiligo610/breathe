package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class ForecastDayData(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherIcon: String,
    val highTempC: Int,
    val lowTempC: Int
)

data class ForecastDayDataExtended(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherIcon: String,
    val highTempC: Double,
    val lowTempC: Double,
    val windSpeedMph: Double,
    val windSpeedDeg: Double,
    val humidityPercent: Double,
)
