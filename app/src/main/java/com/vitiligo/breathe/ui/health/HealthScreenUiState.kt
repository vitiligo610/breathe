package com.vitiligo.breathe.ui.health

import com.vitiligo.breathe.domain.model.ui.HealthZoneUiModel

data class HealthScreenUiState(
    val zones: List<HealthZoneUiModel> = emptyList(),
    val isLoading: Boolean = false
)
