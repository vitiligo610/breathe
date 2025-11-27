package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.local.room.dao.LocationDetailsDao
import com.vitiligo.breathe.data.local.room.dao.UserLocationDao
import com.vitiligo.breathe.data.mapper.toDetailsEntity
import com.vitiligo.breathe.data.mapper.toDomainModel
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.domain.model.ui.LocationDetailsData
import com.vitiligo.breathe.domain.repository.LocationDetailsRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationDetailsRepositoryImpl @Inject constructor(
    private val api: BreatheApi,
    private val detailsDao: LocationDetailsDao,
    private val userLocationDao: UserLocationDao
) : LocationDetailsRepository {

    override fun getLocationDetails(locationId: Int): Flow<LocationDetailsData?> {
        return detailsDao.getLocationDetailsWithLocation(locationId).map { relation ->
            relation?.toDomainModel()
        }
    }

    override suspend fun refreshLocationDetails(locationId: Int): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val targetLocation = userLocationDao.getLocationById(locationId)
                    ?: return@withContext Resource.Error("Location not found in database")

                val responseDto = api.getLocationDetails(
                    latitude = targetLocation.latitude,
                    longitude = targetLocation.longitude
                )

                val detailsEntity = responseDto.toDetailsEntity(locationId)

                detailsDao.insertLocationDetails(detailsEntity)

                Resource.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Failed to refresh details: ${e.localizedMessage}")
            }
        }
    }
}