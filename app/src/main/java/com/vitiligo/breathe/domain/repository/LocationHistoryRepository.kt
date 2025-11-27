package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.local.relation.LocationWithHistory
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface LocationHistoryRepository {
    fun getLocationHistory(locationId: Int): Flow<LocationWithHistory?>
    suspend fun refreshHistory(locationId: Int): Resource<Unit>
}
