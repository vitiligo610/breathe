package com.vitiligo.breathe.ui.location_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitiligo.breathe.domain.repository.LocationDetailsRepository
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
class LocationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LocationDetailsRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<LocationDetails>()
    private val locationId: Int = checkNotNull(route.id)

    private val _isRefreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<LocationDetailsUiState> = combine(
        repository.getLocationDetails(locationId),
        _isRefreshing,
        _error
    ) { data, isRefreshing, error ->
        when {
            data != null -> {
                LocationDetailsUiState.Success(
                    data = data,
                    isRefreshing = isRefreshing
                )
            }
            error != null -> {
                LocationDetailsUiState.Error(error)
            }
            else -> {
                LocationDetailsUiState.Loading
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocationDetailsUiState.Loading
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null

            val result = repository.refreshLocationDetails(locationId)

            if (result is Resource.Error) {
                _error.value = result.message
            }

            _isRefreshing.value = false
        }
    }
}