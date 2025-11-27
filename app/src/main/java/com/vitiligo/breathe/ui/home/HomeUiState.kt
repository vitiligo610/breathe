package com.vitiligo.breathe.ui.home

import com.vitiligo.breathe.domain.model.ui.LocationCardData

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val locations: List<LocationCardData> = emptyList(),
        val isRefreshing: Boolean = false
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}
