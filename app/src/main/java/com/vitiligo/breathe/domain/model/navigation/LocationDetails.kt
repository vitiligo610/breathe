package com.vitiligo.breathe.domain.model.navigation

import kotlinx.serialization.Serializable

@Serializable
data class LocationDetails(
    val id: Int? = null,
    val coordinates: String? = null,
    val placeId: String? = null
)
