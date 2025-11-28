package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.Pollutant

data class LocationDetailsData(
    val locationName: String,
    val localTime: String,
    val aqiCardData: AqiCardData,
    val hourlyForecasts: List<HourlyForecastData>,
    val dailyForecasts: List<ForecastDayDataExtended>,
    val pollutants: List<Pollutant>,
    val placeId: String,
    val locationAdded: Boolean = false,
)
