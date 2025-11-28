package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.domain.model.Coordinates
import com.vitiligo.breathe.domain.model.ui.LocationDetailsData
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface LocationDetailsRepository {

    fun getLocationDetails(locationId: Int?, coordinates: Coordinates?, placeId: String?): Flow<LocationDetailsData?>

    suspend fun refreshLocationDetails(locationId: Int?, coordinates: Coordinates?): Resource<Unit>
}