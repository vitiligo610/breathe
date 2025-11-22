package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class HourlyForecastData(
    val timeLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherIcon: String,
    val tempC: Double,
    val windSpeedMph: Double,
    val windSpeedDeg: Double,
    val humidityPercent: Double
)