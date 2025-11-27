package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class AqiCardData(
    val aqi: Int,
    val category: AqiCategory,
    val dominantPollutant: String,
    val dominantPollutantValue: Double,
    val dominantPollutantUnit: String = "µg/m³",
    val tempC: Double,
    val weatherCode: Int,
    val windSpeedKph: Double,
    val windSpeedDeg: Double,
    val humidityPercent: Double
)
