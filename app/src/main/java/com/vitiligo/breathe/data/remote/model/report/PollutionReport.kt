package com.vitiligo.breathe.data.remote.model.report

import com.google.gson.annotations.SerializedName

data class PollutionReport(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("report_type") val reportType: ReportType,
    val description: String,
    @SerializedName("reported_at") val reportedAt: Long,
    @SerializedName("user_name") val userName: String? = "Anonymous"
)
