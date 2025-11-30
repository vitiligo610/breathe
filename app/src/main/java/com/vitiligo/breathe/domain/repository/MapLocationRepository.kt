package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import com.vitiligo.breathe.domain.util.Resource

interface MapLocationRepository {

    suspend fun getMarkers(
        swLat: Double,
        swLon: Double,
        neLat: Double,
        neLon: Double,
        gridResolution: Int = 20
    ): Resource<List<MapLocationPoint>>
}