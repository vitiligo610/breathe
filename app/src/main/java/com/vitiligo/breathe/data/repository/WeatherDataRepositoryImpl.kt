package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.entity.WeatherData
import com.vitiligo.breathe.data.local.room.dao.WeatherDataDao
import com.vitiligo.breathe.domain.repository.WeatherDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDataRepositoryImpl @Inject constructor(
    private val dao: WeatherDataDao
) : WeatherDataRepository {
    override fun getLatestWeatherDataForLocation(locationId: Int): Flow<WeatherData?> {
        return dao.getLatestWeatherDataForLocation(locationId)
    }

    override fun getWeatherHistoryForLocation(locationId: Int): Flow<List<WeatherData>> {
        return dao.getWeatherHistoryForLocation(locationId)
    }

    override suspend fun insertWeatherData(weatherData: WeatherData) {
        dao.insertWeatherData(weatherData)
    }
}