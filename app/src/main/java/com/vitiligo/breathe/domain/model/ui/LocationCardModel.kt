package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory

data class ForecastDay(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherIcon: String,
    val highTempC: Int,
    val lowTempC: Int
)

data class LocationCardData(
    val id: Int,
    val city: String,
    val subtitle: String,
    val currentAqi: Int,
    val currentAqiCategory: AqiCategory,
    val currentTempC: Int,
    val currentWeatherIcon: String,
    val dominantPollutant: String,
    val forecasts: List<ForecastDay>,
    val time: String
)