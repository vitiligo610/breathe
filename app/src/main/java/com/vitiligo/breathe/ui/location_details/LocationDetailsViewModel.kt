package com.vitiligo.breathe.ui.location_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitiligo.breathe.domain.repository.LocationDetailsRepository
import com.vitiligo.breathe.domain.util.Resource
import com.vitiligo.breathe.domain.model.navigation.LocationDetails
import com.vitiligo.breathe.domain.repository.LocationSummaryRepository
import com.vitiligo.breathe.domain.util.toCoordinates
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
    private val summaryRepository: LocationSummaryRepository,
    private val detailsRepository: LocationDetailsRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<LocationDetails>()
    private val locationId = route.id
    private val coordinates = route.coordinates
    private val placeId = route.placeId

    private val _isRefreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<LocationDetailsUiState> = combine(
        detailsRepository.getLocationDetails(locationId, coordinates?.toCoordinates(), placeId),
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

            val result = detailsRepository.refreshLocationDetails(locationId, coordinates?.toCoordinates())

            if (result is Resource.Error) {
                _error.value = result.message
            }

            _isRefreshing.value = false
        }
    }

    fun addLocation(callback: (() -> Unit)?) {
        val safeCoordinates = checkNotNull(coordinates).toCoordinates()
        val placeId = checkNotNull(route.placeId)

        viewModelScope.launch {
            summaryRepository.addLocation(safeCoordinates, placeId)

            callback?.invoke()
        }
    }

    fun removeLocation(callback: (() -> Unit)?) {
        val placeId = if (state.value is LocationDetailsUiState.Success) (state.value as LocationDetailsUiState.Success).data.placeId else null

        if (placeId != null) {
            viewModelScope.launch {
                summaryRepository.removeLocationByPlaceId(placeId)
            }

            callback?.invoke()
        }
    }
}