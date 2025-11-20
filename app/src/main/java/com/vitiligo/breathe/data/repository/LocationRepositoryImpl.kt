package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.room.dao.LocationDao
import com.vitiligo.breathe.data.local.entity.Location
import com.vitiligo.breathe.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dao: LocationDao
) : LocationRepository {

    override fun getAllLocations(): Flow<List<Location>> {
        return dao.getAllLocations()
    }

    override fun getLocationById(id: Int): Flow<Location> {
        return dao.getLocationById(id)
    }

    override suspend fun insertLocation(location: Location) {
        dao.insertLocation(location)
    }

    override suspend fun updateLocation(location: Location) {
        dao.updateLocation(location)
    }

    override suspend fun removeLocation(location: Location) {
        dao.removeLocation(location)
    }
}