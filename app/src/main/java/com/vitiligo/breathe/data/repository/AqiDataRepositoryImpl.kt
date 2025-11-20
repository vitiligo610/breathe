package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.entity.AqiData
import com.vitiligo.breathe.data.local.room.dao.AqiDataDao
import com.vitiligo.breathe.domain.repository.AqiDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AqiDataRepositoryImpl @Inject constructor(
    private val dao: AqiDataDao
) : AqiDataRepository {
    override fun getLatestAqiDataForLocation(locationId: Int): Flow<AqiData?> {
        return dao.getLatestAqiDataForLocation(locationId)
    }

    override fun getAqiHistoryForLocation(locationId: Int): Flow<List<AqiData>> {
        return dao.getAqiHistoryForLocation(locationId)
    }

    override suspend fun insertAqiData(aqiData: AqiData) {
        dao.insertAqiData(aqiData)
    }
}