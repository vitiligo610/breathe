package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.domain.model.ui.LocationDetailsData
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface LocationDetailsRepository {

    fun getLocationDetails(locationId: Int): Flow<LocationDetailsData?>

    suspend fun refreshLocationDetails(locationId: Int): Resource<Unit>
}