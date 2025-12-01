package com.vitiligo.breathe.domain.model

data class AddressInfo(
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val country: String,
    val postalCode: String? = null,
    val thoroughfare: String? = null,
    val featureName: String? = null
)
