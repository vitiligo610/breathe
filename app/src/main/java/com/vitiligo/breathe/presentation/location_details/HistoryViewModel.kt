package com.vitiligo.breathe.presentation.location_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitiligo.breathe.data.mapper.extractPoints
import com.vitiligo.breathe.data.mapper.getAvailableTabs
import com.vitiligo.breathe.data.remote.model.HistoryForecast
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.model.ui.HistoryViewMode
import com.vitiligo.breathe.domain.repository.LocationHistoryRepository
import com.vitiligo.breathe.domain.util.Resource
import com.vitiligo.breathe.ui.navigation.LocationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LocationHistoryRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<LocationDetails>()
    private val locationId: Int = checkNotNull(route.id)

    private val _viewMode = MutableStateFlow(HistoryViewMode.Hourly)
    private val _selectedTab = MutableStateFlow(HistoryTabOption.AQI)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<HistoryUiState> = combine(
        repository.getLocationHistory(locationId),
        _viewMode,
        _selectedTab,
        _isLoading,
        _error
    ) { dbData, mode, tab, loading, error ->

        val history = dbData?.history
        val offset = dbData?.location?.utcOffsetSeconds ?: 0

        val hTabs = history?.hourlyForecast?.getAvailableTabs() ?: emptyList()
        val dTabs = history?.dailyForecast?.getAvailableTabs() ?: emptyList()

        val activeForecast: HistoryForecast? = if (mode == HistoryViewMode.Hourly) {
            history?.hourlyForecast
        } else {
            history?.dailyForecast
        }

        val validTabs = if (mode == HistoryViewMode.Hourly) hTabs else dTabs
        val effectiveTab = if (validTabs.contains(tab)) tab else validTabs.firstOrNull() ?: HistoryTabOption.AQI

        val points = activeForecast?.extractPoints(effectiveTab, offset) ?: emptyList()

        HistoryUiState(
            isLoading = loading,
            error = error,
            viewMode = mode,
            selectedTab = effectiveTab,
            chartData = points,
            hourlyTabs = hTabs,
            dailyTabs = dTabs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState(isLoading = true)
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.refreshHistory(locationId)
            if (result is Resource.Error) {
                _error.value = result.message
            }
            _isLoading.value = false
        }
    }

    fun setViewMode(mode: HistoryViewMode) {
        _viewMode.value = mode
    }

    fun setSelectedTab(tab: HistoryTabOption) {
        _selectedTab.value = tab
    }
}