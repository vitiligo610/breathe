package com.vitiligo.breathe.domain.model

import androidx.annotation.DrawableRes

data class ForecastDay(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    @DrawableRes val weatherIconRes: Int,
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
    @DrawableRes val currentWeatherIconRes: Int,
    val dominantPollutant: String,
    val forecasts: List<ForecastDay>
)