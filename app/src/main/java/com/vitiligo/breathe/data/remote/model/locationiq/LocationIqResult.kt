package com.vitiligo.breathe.data.remote.model.locationiq

import com.google.gson.annotations.SerializedName

data class LocationIqResult(
    @SerializedName("place_id") val placeId: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("address") val address: LocationIqAddress?
)

data class LocationIqAddress(
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("country_code") val countryCode: String?
)