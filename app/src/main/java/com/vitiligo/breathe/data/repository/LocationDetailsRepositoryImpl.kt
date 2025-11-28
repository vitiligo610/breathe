package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.room.dao.LocationDetailsDao
import com.vitiligo.breathe.data.local.room.dao.UserLocationDao
import com.vitiligo.breathe.data.mapper.toDetailsEntity
import com.vitiligo.breathe.data.mapper.toDomainModel
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.domain.model.Coordinates
import com.vitiligo.breathe.domain.model.ui.LocationDetailsData
import com.vitiligo.breathe.domain.repository.LocationDetailsRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationDetailsRepositoryImpl @Inject constructor(
    private val api: BreatheApi,
    private val detailsDao: LocationDetailsDao,
    private val userLocationDao: UserLocationDao
) : LocationDetailsRepository {

    private val _previewData = MutableStateFlow<LocationDetailsData?>(null)

    override fun getLocationDetails(locationId: Int?, coordinates: Coordinates?, placeId: String?): Flow<LocationDetailsData?> {
        return flow {
            var lId = locationId

            if (placeId != null && lId == null) {
                val location = userLocationDao.getLocationByPlaceId(placeId)
                if (location != null) {
                    lId = location.id
                }
            }

            val targetFlow: Flow<LocationDetailsData?> = when {
                lId != null -> {
                    detailsDao.getLocationDetailsWithLocation(lId).map { relation ->
                        relation?.toDomainModel()
                    }
                }
                coordinates != null -> {
                    _previewData.asStateFlow()
                }
                else -> flowOf(null)
            }

            emitAll(targetFlow)
        }
    }

    override suspend fun refreshLocationDetails(locationId: Int?, coordinates: Coordinates?): Resource<Unit> {
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

                val response = api.getLocationDetails(targetLat, targetLng)

                if (locationId != null) {
                    val detailsEntity = response.toDetailsEntity(locationId)
                    detailsDao.insertLocationDetails(detailsEntity)
                    _previewData.emit(null)
                } else {
                    val domainModel = response.toDomainModel()
                    _previewData.emit(domainModel)
                }

                Resource.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Failed to refresh details: ${e.localizedMessage}")
            }
        }
    }
}