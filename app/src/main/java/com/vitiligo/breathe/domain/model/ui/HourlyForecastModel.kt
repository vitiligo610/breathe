package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class HourlyForecastData(
    val timeLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherCode: Int,
    val tempC: Double,
    val windSpeedKph: Double,
    val windSpeedDeg: Double,
    val humidityPercent: Double
)