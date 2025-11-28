package com.vitiligo.breathe.domain.model

data class LocationSearchResult(
    val id: String,
    val name: String,
    val region: String,
    val latitude: Double,
    val longitude: Double,
    val countryCode: String,
)
