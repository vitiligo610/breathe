package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.ForecastDayDataExtended

val mockDailyForecastData: List<ForecastDayDataExtended> = listOf(
    ForecastDayDataExtended(
        dayLabel = "Today",
        aqi = 25,
        aqiCategory = AqiCategory.GREEN,
        weatherIcon = "01d", // clear sky day
        highTempC = 28.5,
        lowTempC = 15.2,
        windSpeedMph = 4.7,
        windSpeedDeg = 270.0,
        humidityPercent = 55.0,
    ),
    ForecastDayDataExtended(
        dayLabel = "Sun",
        aqi = 58,
        aqiCategory = AqiCategory.YELLOW,
        weatherIcon = "04d",
        highTempC = 26.1,
        lowTempC = 14.8,
        windSpeedMph = 7.1,
        windSpeedDeg = 300.0,
        humidityPercent = 60.0,
    ),
    ForecastDayDataExtended(
        dayLabel = "Mon",
        aqi = 105,
        aqiCategory = AqiCategory.ORANGE,
        weatherIcon = "10d",
        highTempC = 22.0,
        lowTempC = 13.5,
        windSpeedMph = 12.5,
        windSpeedDeg = 180.0,
        humidityPercent = 85.0,
    ),
    ForecastDayDataExtended(
        dayLabel = "Tue",
        aqi = 155,
        aqiCategory = AqiCategory.RED,
        weatherIcon = "13d",
        highTempC = 15.0,
        lowTempC = 8.0,
        windSpeedMph = 9.0,
        windSpeedDeg = 45.0,
        humidityPercent = 90.0,
    ),
    ForecastDayDataExtended(
        dayLabel = "Wed",
        aqi = 210,
        aqiCategory = AqiCategory.PURPLE,
        weatherIcon = "50d",
        highTempC = 18.3,
        lowTempC = 9.1,
        windSpeedMph = 2.2,
        windSpeedDeg = 90.0,
        humidityPercent = 95.0,
    ),
    ForecastDayDataExtended(
        dayLabel = "Thu",
        aqi = 305,
        aqiCategory = AqiCategory.MAROON,
        weatherIcon = "03d",
        highTempC = 20.7,
        lowTempC = 10.5,
        windSpeedMph = 6.8,
        windSpeedDeg = 135.0,
        humidityPercent = 70.0,
    ),
    ForecastDayDataExtended(
        dayLabel = "Fri",
        aqi = 40,
        aqiCategory = AqiCategory.GREEN,
        weatherIcon = "09d",
        highTempC = 25.4,
        lowTempC = 14.0,
        windSpeedMph = 10.1,
        windSpeedDeg = 225.0,
        humidityPercent = 75.0,
    )
)