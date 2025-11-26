package com.vitiligo.breathe.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseLocationResponse {
    @SerialName("name")
    var name: String? = null

    @SerialName("country")
    var country: String? = null

    @SerialName("latitude")
    var latitude: Double? = null

    @SerialName("longitude")
    var longitude: Double? = null

    @SerialName("timezone")
    var timezone: String? = null

    @SerialName("timestamp")
    var timestamp: Long? = null

    @SerialName("utc_offset_seconds")
    var utcOffsetSeconds: Int? = null
}
