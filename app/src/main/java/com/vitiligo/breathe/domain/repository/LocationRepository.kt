package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.local.entity.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getAllLocations(): Flow<List<Location>>
    fun getLocationById(id: Int): Flow<Location>
    suspend fun insertLocation(location: Location)
    suspend fun updateLocation(location: Location)
    suspend fun removeLocation(location: Location)
}
