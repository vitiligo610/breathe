package com.vitiligo.breathe.ui.map

import com.vitiligo.breathe.data.remote.model.MapLocationPoint

data class MapUiState(
    val mapPoints: List<MapLocationPoint> = emptyList(),
    val selectedPoint: SelectedPointInfo? = null,
)

data class SelectedPointInfo(
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val country: String,
    val aqi: Int,
)
