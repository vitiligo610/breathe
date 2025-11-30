package com.vitiligo.breathe.ui.map

import android.os.Parcelable
import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import kotlinx.parcelize.Parcelize

data class MapUiState(
    val mapPoints: List<MapLocationPoint> = emptyList(),
    val selectedPoint: SelectedPointInfo? = null
)

data class SelectedPointInfo(
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val country: String,
    val aqi: Int,
)

@Parcelize
data class MapCameraState(
    val latitude: Double,
    val longitude: Double,
    val zoom: Double
): Parcelable
