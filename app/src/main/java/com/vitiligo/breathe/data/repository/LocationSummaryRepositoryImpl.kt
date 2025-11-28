package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.entity.UserLocation
import com.vitiligo.breathe.data.local.room.dao.LocationSummaryDao
import com.vitiligo.breathe.data.mapper.toDomainModel
import com.vitiligo.breathe.data.mapper.toSummaryEntity
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.domain.model.Coordinates
import com.vitiligo.breathe.domain.model.ui.LocationCardData
import com.vitiligo.breathe.domain.repository.LocationSummaryRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationSummaryRepositoryImpl @Inject constructor(
    private val api: BreatheApi,
    private val dao: LocationSummaryDao
) : LocationSummaryRepository {

    override fun getSavedLocations(): Flow<List<LocationCardData>> {
        return dao.getLocationsWithSummary().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun refreshData(): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                var userLocations = dao.getAllUserLocations()

                if (userLocations.isEmpty()) {
                    val defaultLocations = listOf(
                        // Karachi
                        UserLocation(
                            latitude = 24.8607, longitude = 67.0011,
                            name = "Karachi", country = "Pakistan",
                            timezone = "Asia/Karachi", utcOffsetSeconds = 18000
                        ),
                        // Lahore
                        UserLocation(
                            latitude = 31.5204, longitude = 74.3587,
                            name = "Lahore", country = "Pakistan",
                            timezone = "Asia/Karachi", utcOffsetSeconds = 18000
                        ),
                        // Islamabad
                        UserLocation(
                            latitude = 33.6844, longitude = 73.0479,
                            name = "Islamabad", country = "Pakistan",
                            timezone = "Asia/Karachi", utcOffsetSeconds = 18000
                        )
                    )

                    defaultLocations.forEach { dao.insertUserLocation(it) }

                    userLocations = dao.getAllUserLocations()
                }

                val deferredSummaries = userLocations.map { location ->
                    async {
                        try {
                            val dto = api.getLocationSummary(location.latitude, location.longitude)
                            dto.toSummaryEntity(parentId = location.id)
                        } catch (e: Exception) {
                            println("Failed to fetch summary for location ${location.id}: ${e.message}")
                            null
                        }
                    }
                }

                val resultsWithNulls = deferredSummaries.awaitAll()
                val newSummaries = resultsWithNulls.filterNotNull()

                if (newSummaries.isNotEmpty()) {
                    dao.updateSummaries(newSummaries)
                }

                Resource.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Failed to refresh data: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun addLocation(coordinates: Coordinates, placeId: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val latitude = coordinates.latitude
                val longitude = coordinates.longitude

                val dto = api.getLocationSummary(latitude, longitude)

                val locationEntity = UserLocation(
                    latitude = dto.latitude ?: latitude,
                    longitude = dto.longitude ?: longitude,
                    name = dto.name ?: "Unknown",
                    country = dto.country ?: "",
                    timezone = dto.timezone ?: "UTC",
                    utcOffsetSeconds = dto.utcOffsetSeconds ?: 0,
                    placeId = placeId
                )

                val newId = dao.insertUserLocation(locationEntity)

                if (newId != -1L) {
                    val summaryEntity = dto.toSummaryEntity(parentId = newId.toInt())
                    dao.insertLocationSummary(summaryEntity)
                }

                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error("Could not add location: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun removeLocation(id: Int) {
        // Implement deletion logic in DAO if needed
    }
}