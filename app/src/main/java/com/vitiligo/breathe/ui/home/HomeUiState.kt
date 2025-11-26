package com.vitiligo.breathe.ui.home

import com.vitiligo.breathe.domain.model.ui.LocationCardData

data class HomeUiState(
    val locations: List<LocationCardData> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)
