package com.vitiligo.breathe.data.remote.model

import com.google.gson.annotations.SerializedName

data class MapLocationPoint(
    val latitude: Double,
    val longitude: Double,
    val aqi: Int,

    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,

    @SerializedName("is_cluster")
    val isCluster: Boolean,

    @SerializedName("point_count")
    val pointCount: Int
)
