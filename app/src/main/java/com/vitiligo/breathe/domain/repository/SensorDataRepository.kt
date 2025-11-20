package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.local.entity.SensorData
import kotlinx.coroutines.flow.Flow

interface SensorDataRepository {
    fun getLatestSensorDataForLocation(locationId: Int): Flow<SensorData?>
    fun getSensorHistoryForLocation(locationId: Int): Flow<List<SensorData>>
    suspend fun insertSensorData(sensorData: SensorData)
}