package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.domain.model.Coordinates
import com.vitiligo.breathe.domain.model.ui.LocationCardData
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface LocationSummaryRepository {

    fun getSavedLocations(): Flow<List<LocationCardData>>
    suspend fun refreshData(): Resource<Unit>
    suspend fun addLocation(coordinates: Coordinates, placeId: String): Resource<Unit>
    suspend fun removeLocation(id: Int)
}
