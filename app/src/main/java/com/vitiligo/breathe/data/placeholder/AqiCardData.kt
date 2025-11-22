package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.AqiCardData

val mockAqiCardData = AqiCardData(
    aqi = 150,
    category = AqiCategory.ORANGE,
    mainPollutant = "PM2.5",
    mainPollutantConcentration = 9.1,
    tempC = 7.0,
    weatherIcon = "01n",
    windSpeedMph = 5.5,
    windSpeedDeg = 351.0,
    humidityPercent = 54.0
)
