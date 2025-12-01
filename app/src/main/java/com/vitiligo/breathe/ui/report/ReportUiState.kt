package com.vitiligo.breathe.ui.report

import com.vitiligo.breathe.data.remote.model.report.ReportType

data class ReportUiState(
    val latitude: Double,
    val longitude: Double,
    val city: String = "Unknown City",
    val country: String = "Unknown Country",
    val selectedType: ReportType = ReportType.BURNING,
    val description: String = "",
    val isLoading: Boolean = false,
    val submitResult: Boolean? = null
)
