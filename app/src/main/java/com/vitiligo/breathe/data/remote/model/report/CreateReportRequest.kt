package com.vitiligo.breathe.data.remote.model.report

import com.google.gson.annotations.SerializedName

data class CreateReportRequest(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("report_type") val reportType: ReportType,
    val description: String
)
