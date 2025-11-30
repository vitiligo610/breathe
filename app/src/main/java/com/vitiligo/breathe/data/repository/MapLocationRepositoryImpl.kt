package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import com.vitiligo.breathe.domain.repository.MapLocationRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapLocationRepositoryImpl @Inject constructor(
    private val api: BreatheApi
): MapLocationRepository {

    override suspend fun getMarkers(
        swLat: Double,
        swLon: Double,
        neLat: Double,
        neLon: Double,
        gridResolution: Int
    ): Resource<List<MapLocationPoint>> {

        return withContext(Dispatchers.IO){
            try {
                val bbox = "$swLat,$swLon,$neLat,$neLon"

                val response = api.getLocationMap(bbox, gridResolution)

                Resource.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.localizedMessage ?: "Failed to fetch map data")
            }
        }
    }
}