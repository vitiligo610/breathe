package com.vitiligo.breathe.data.placeholder

import com.vitiligo.breathe.domain.model.Pollutant
import com.vitiligo.breathe.domain.util.getPollutantDetails

val mockAirPollutantsData: List<Pollutant> = listOf(
    getPollutantDetails(
        key = "pm2_5",
        reading = 4.3
    ),
    getPollutantDetails(
        key = "pm10",
        reading = 13.1
    ),
    getPollutantDetails(
        key = "o3",
        reading = 14.0
    ),
    getPollutantDetails(
        key = "no2",
        reading = 6.0
    ),
    getPollutantDetails(
        key = "so2",
        reading = 0.7
    ),
    getPollutantDetails(
        key = "co",
        reading = 0.2
    )
)
