package com.vitiligo.breathe.ui.search

import com.vitiligo.breathe.domain.model.LocationSearchResult

data class LocationSearchUiState(
    val query: String = "",
    val results: List<LocationSearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
