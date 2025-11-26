package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.ForecastDayData
import com.vitiligo.breathe.domain.model.ui.LocationCardData

val placeholderLocationCardData = listOf(
    LocationCardData(
        id = 1,
        city = "Karachi",
        subtitle = "Sindh, Pakistan",
        currentAqi = 199,
        currentAqiCategory = AqiCategory.YELLOW,
        currentTempC = 18,
        currentWeatherCode = 0,
        forecasts = listOf(
            ForecastDayData(
                dayLabel = "Today",
                aqi = 167,
                aqiCategory = AqiCategory.RED,
                weatherCode = 3,
                highTempC = 26,
                lowTempC = 18
            ),
            ForecastDayData(
                dayLabel = "Fri",
                aqi = 79,
                aqiCategory = AqiCategory.YELLOW,
                weatherCode = 2,
                highTempC = 27,
                lowTempC = 20
            ),
            ForecastDayData(
                dayLabel = "Sat",
                aqi = 86,
                aqiCategory = AqiCategory.YELLOW,
                weatherCode = 1,
                highTempC = 27,
                lowTempC = 23
            )
        ),
        time = "18:00"
    ),
    LocationCardData(
        id = 2,
        city = "Lahore",
        subtitle = "Punjab, Pakistan",
        currentAqi = 45,
        currentAqiCategory = AqiCategory.ORANGE,
        currentTempC = 12,
        currentWeatherCode = 2,
        forecasts = listOf(
            ForecastDayData(
                dayLabel = "Today",
                aqi = 50,
                aqiCategory = AqiCategory.GREEN,
                weatherCode = 1,
                highTempC = 15,
                lowTempC = 8
            ),
            ForecastDayData(
                dayLabel = "Sat",
                aqi = 65,
                aqiCategory = AqiCategory.YELLOW,
                weatherCode = 3,
                highTempC = 18,
                lowTempC = 10
            )
        ),
        time = "12:00"
    )
)