package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.local.entity.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherDataRepository {
    fun getLatestWeatherDataForLocation(locationId: Int): Flow<WeatherData?>
    fun getWeatherHistoryForLocation(locationId: Int): Flow<List<WeatherData>>
    suspend fun insertWeatherData(weatherData: WeatherData)
}