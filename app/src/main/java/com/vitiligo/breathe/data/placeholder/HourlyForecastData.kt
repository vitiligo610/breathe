package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.HourlyForecastData

val mockHourlyForecastData = listOf(
    HourlyForecastData("Now", 50, AqiCategory.GREEN, 9, 7.0, 5.5, 180.0, 54.0),
    HourlyForecastData("14:00", 49, AqiCategory.GREEN, 1, 7.0, 3.6, 120.0, 52.0),
    HourlyForecastData("15:00", 46, AqiCategory.GREEN, 2, 6.0, 3.6, 300.0, 48.0),
    HourlyForecastData("16:00", 44, AqiCategory.GREEN, 4, 6.0, 0.0, 0.0, 45.0),
    HourlyForecastData("17:00", 42, AqiCategory.GREEN, 3, 5.0, 0.0, 220.0, 43.0),
    HourlyForecastData("18:00", 40, AqiCategory.GREEN, 5, 4.0, 1.2, 90.0, 40.0),
)