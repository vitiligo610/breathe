package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class AqiCardData(
    val aqi: Int,
    val category: AqiCategory,
    val mainPollutant: String,
    val mainPollutantConcentration: Double,
    val mainPollutantConcentrationUnit: String = "µg/m³",
    val tempC: Double,
    val weatherStatus: String,
    val windSpeedMps: Double,
    val windSpeedDeg: Double,
    val humidityPercent: Double
)
