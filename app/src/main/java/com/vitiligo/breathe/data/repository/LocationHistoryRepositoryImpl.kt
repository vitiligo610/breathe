package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.relation.LocationWithHistory
import com.vitiligo.breathe.data.local.room.dao.LocationHistoryDao
import com.vitiligo.breathe.data.local.room.dao.UserLocationDao
import com.vitiligo.breathe.data.mapper.toHistoryEntity
import com.vitiligo.breathe.data.mapper.toUserLocation
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.domain.model.Coordinates
import com.vitiligo.breathe.domain.repository.LocationHistoryRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationHistoryRepositoryImpl @Inject constructor(
    private val api: BreatheApi,
    private val historyDao: LocationHistoryDao,
    private val userLocationDao: UserLocationDao
) : LocationHistoryRepository {

    private val _previewData = MutableStateFlow<LocationWithHistory?>(null)

    override fun getLocationHistory(locationId: Int?, coordinates: Coordinates?): Flow<LocationWithHistory?> {
        return when {
            locationId != null -> {
                historyDao.getLocationWithHistory(locationId)
            }
            coordinates != null -> {
                _previewData.asStateFlow()
            }
            else -> {
                flowOf(null)
            }
        }
    }

    override suspend fun refreshHistory(locationId: Int?, coordinates: Coordinates?): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val targetLat: Double
                val targetLng: Double

                if (locationId != null) {
                    val allLocations = userLocationDao.getAllUserLocations()
                    val savedLocation = allLocations.find { it.id == locationId }
                        ?: return@withContext Resource.Error("Location not found in database")
                    targetLat = savedLocation.latitude
                    targetLng = savedLocation.longitude
                } else if (coordinates != null) {
                    targetLat = coordinates.latitude
                    targetLng = coordinates.longitude
                } else {
                    return@withContext Resource.Error("No location ID or coordinates provided")
                }

                val response = api.getLocationHistory(targetLat, targetLng)

                if (locationId != null) {
                    val historyEntity = response.toHistoryEntity(locationId)
                    historyDao.insertLocationHistory(historyEntity)
                    _previewData.emit(null)
                } else {
                    _previewData.emit(
                        LocationWithHistory(
                            location = response.toUserLocation(),
                            history = response.toHistoryEntity(-1)
                        )
                    )
                }

                Resource.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.localizedMessage ?: "Failed to fetch history")
            }
        }
    }
}