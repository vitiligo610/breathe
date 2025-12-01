package com.vitiligo.breathe.data.remote.model

import com.google.gson.annotations.SerializedName
import com.vitiligo.breathe.data.remote.model.report.ReportType

data class MapLocationPoint(
    val latitude: Double,
    val longitude: Double,

    @SerializedName("marker_type")
    val markerType: String,

    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int? = 0,

    val aqi: Int? = null,
    @SerializedName("is_cluster")
    val isCluster: Boolean = false,
    @SerializedName("point_count")
    val pointCount: Int? = 0,

    @SerializedName("report_id")
    val reportId: Long? = null,
    @SerializedName("report_type")
    val reportType: ReportType? = null,
    @SerializedName("report_description")
    val reportDescription: String? = null,
    @SerializedName("reported_at")
    val reportedAt: Long? = null
)
