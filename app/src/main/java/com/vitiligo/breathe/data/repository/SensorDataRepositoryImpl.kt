package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.entity.SensorData
import com.vitiligo.breathe.data.local.room.dao.SensorDataDao
import com.vitiligo.breathe.domain.repository.SensorDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SensorDataRepositoryImpl @Inject constructor(
    private val dao: SensorDataDao
) : SensorDataRepository {
    override fun getLatestSensorDataForLocation(locationId: Int): Flow<SensorData?> {
        return dao.getLatestSensorDataForLocation(locationId)
    }

    override fun getSensorHistoryForLocation(locationId: Int): Flow<List<SensorData>> {
        return dao.getSensorHistoryForLocation(locationId)
    }

    override suspend fun insertSensorData(sensorData: SensorData) {
        dao.insertSensorData(sensorData)
    }
}