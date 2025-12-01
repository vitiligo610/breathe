package com.vitiligo.breathe.ui.map

import android.os.Parcelable
import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import com.vitiligo.breathe.data.remote.model.report.ReportType
import kotlinx.parcelize.Parcelize

data class MapUiState(
    val activeMarkerType: String = "aqi",
    val mapPoints: List<MapLocationPoint> = emptyList(),
    val selectedPoint: SelectedPointInfo? = null
)

data class SelectedPointInfo(
    val latitude: Double,
    val longitude: Double,
    val city: String = "",
    val country: String = "",

    val type: String,

    val aqi: Int? = null,
    val isCluster: Boolean = false,

    val reportType: ReportType? = null,
    val reportDescription: String? = null,
    val reportedAt: Long? = null
)

@Parcelize
data class MapCameraState(
    val latitude: Double,
    val longitude: Double,
    val zoom: Double
): Parcelable
