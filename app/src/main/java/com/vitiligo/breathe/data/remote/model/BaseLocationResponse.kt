package com.vitiligo.breathe.data.remote.model

import com.google.gson.annotations.SerializedName

open class BaseLocationResponse {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("latitude")
    var latitude: Double? = null

    @SerializedName("longitude")
    var longitude: Double? = null

    @SerializedName("timezone")
    var timezone: String? = null

    @SerializedName("timestamp")
    var timestamp: Long? = null

    @SerializedName("utc_offset_seconds")
    var utcOffsetSeconds: Int? = null
}
