package com.vitiligo.breathe.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitiligo.breathe.domain.repository.LocationSummaryRepository
import com.vitiligo.breathe.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: LocationSummaryRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<HomeUiState> = combine(
        repository.getSavedLocations(),
        _isRefreshing,
        _error
    ) { locations, isRefreshing, error ->
            HomeUiState(
                locations = locations,
                isRefreshing = isRefreshing,
                error = error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isRefreshing = true)
        )

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null

            val result = repository.refreshData()

            if (result is Resource.Error) {
                _error.value = result.message
            }

            _isRefreshing.value = false
        }
    }
}