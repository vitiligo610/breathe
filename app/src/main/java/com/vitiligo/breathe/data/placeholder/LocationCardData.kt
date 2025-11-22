package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.ForecastDay
import com.vitiligo.breathe.domain.model.ui.LocationCardData

val placeholderLocationCardData = listOf(
    LocationCardData(
        id = 1,
        city = "Karachi",
        subtitle = "Sindh, Pakistan",
        currentAqi = 199,
        currentAqiCategory = AqiCategory.YELLOW,
        currentTempC = 18,
        currentWeatherIcon = "01d",
        dominantPollutant = "PM2.5",
        forecasts = listOf(
            ForecastDay(
                dayLabel = "Today",
                aqi = 167,
                aqiCategory = AqiCategory.RED,
                weatherIcon = "02d",
                highTempC = 26,
                lowTempC = 18
            ),
            ForecastDay(
                dayLabel = "Fri",
                aqi = 79,
                aqiCategory = AqiCategory.YELLOW,
                weatherIcon = "01d",
                highTempC = 27,
                lowTempC = 20
            ),
            ForecastDay(
                dayLabel = "Sat",
                aqi = 86,
                aqiCategory = AqiCategory.YELLOW,
                weatherIcon = "01d",
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
        currentWeatherIcon = "02d",
        dominantPollutant = "O3",
        forecasts = listOf(
            ForecastDay(
                dayLabel = "Today",
                aqi = 50,
                aqiCategory = AqiCategory.GREEN,
                weatherIcon = "01d",
                highTempC = 15,
                lowTempC = 8
            ),
            ForecastDay(
                dayLabel = "Sat",
                aqi = 65,
                aqiCategory = AqiCategory.YELLOW,
                weatherIcon = "04d",
                highTempC = 18,
                lowTempC = 10
            )
        ),
        time = "12:00"
    )
)