package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.local.entity.AqiData
import kotlinx.coroutines.flow.Flow

interface AqiDataRepository {
    fun getLatestAqiDataForLocation(locationId: Int): Flow<AqiData?>
    fun getAqiHistoryForLocation(locationId: Int): Flow<List<AqiData>>
    suspend fun insertAqiData(aqiData: AqiData)
}
