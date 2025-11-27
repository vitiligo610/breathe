package com.vitiligo.breathe.ui.location_details

import com.vitiligo.breathe.domain.model.ui.LocationDetailsData

sealed interface LocationDetailsUiState {
    data object Loading : LocationDetailsUiState

    data class Success(
        val data: LocationDetailsData,
        val isRefreshing: Boolean = false
    ) : LocationDetailsUiState

    data class Error(val message: String) : LocationDetailsUiState
}
