package com.vitiligo.breathe.presentation.location_details

import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.model.ui.HistoryViewMode

data class HistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val viewMode: HistoryViewMode = HistoryViewMode.Hourly,
    val selectedTab: HistoryTabOption = HistoryTabOption.AQI,

    val chartData: List<HistoryPoint> = emptyList(),

    val hourlyTabs: List<HistoryTabOption> = emptyList(),
    val dailyTabs: List<HistoryTabOption> = emptyList()
)
