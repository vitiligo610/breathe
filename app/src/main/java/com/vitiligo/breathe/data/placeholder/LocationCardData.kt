package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.R
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ForecastDay
import com.vitiligo.breathe.domain.model.LocationCardData
import com.vitiligo.breathe.domain.util.getWeatherIconRes

val placeholderLocationCardData = listOf(
    LocationCardData(
        id = 1,
        city = "Karachi",
        subtitle = "Sindh, Pakistan",
        currentAqi = 199,
        currentAqiCategory = AqiCategory.ORANGE,
        currentTempC = 18,
        currentWeatherIconRes = R.drawable.ic_weather_few_clouds_full_32,
        dominantPollutant = "PM2.5",
        forecasts = listOf(
            ForecastDay(
                dayLabel = "Today",
                aqi = 167,
                aqiCategory = AqiCategory.RED,
                weatherIconRes = getWeatherIconRes("02d"),
                highTempC = 26,
                lowTempC = 18
            ),
            ForecastDay(
                dayLabel = "Fri",
                aqi = 79,
                aqiCategory = AqiCategory.YELLOW,
                weatherIconRes = getWeatherIconRes("01d"),
                highTempC = 27,
                lowTempC = 20
            ),
            ForecastDay(
                dayLabel = "Sat",
                aqi = 86,
                aqiCategory = AqiCategory.YELLOW,
                weatherIconRes = getWeatherIconRes("01d"),
                highTempC = 27,
                lowTempC = 23
            )
        )
    ),
    LocationCardData(
        id = 2,
        city = "Lahore",
        subtitle = "Punjab, Pakistan",
        currentAqi = 45,
        currentAqiCategory = AqiCategory.GREEN,
        currentTempC = 12,
        currentWeatherIconRes = R.drawable.ic_weather_clear_sky_full_32,
        dominantPollutant = "O3",
        forecasts = listOf(
            ForecastDay(
                dayLabel = "Today",
                aqi = 50,
                aqiCategory = AqiCategory.GREEN,
                weatherIconRes = getWeatherIconRes("01d"),
                highTempC = 15,
                lowTempC = 8
            ),
            ForecastDay(
                dayLabel = "Sat",
                aqi = 65,
                aqiCategory = AqiCategory.YELLOW,
                weatherIconRes = getWeatherIconRes("04d"),
                highTempC = 18,
                lowTempC = 10
            )
        )
    )
)