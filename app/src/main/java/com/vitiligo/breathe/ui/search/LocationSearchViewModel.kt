package com.vitiligo.breathe.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitiligo.breathe.domain.model.Coordinates
import com.vitiligo.breathe.domain.model.LocationSearchResult
import com.vitiligo.breathe.domain.repository.LocationSearchRepository
import com.vitiligo.breathe.domain.repository.LocationSummaryRepository
import com.vitiligo.breathe.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: LocationSearchRepository,
    private val locationRepository: LocationSummaryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LocationSearchUiState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)

        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _state.value = _state.value.copy(results = emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            delay(500L)
            performSearch(newQuery)
        }
    }

    private suspend fun performSearch(query: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        when (val result = searchRepository.search(query)) {
            is Resource.Success -> {
                _state.value = _state.value.copy(
                    results = result.data ?: emptyList(),
                    isLoading = false
                )
            }
            is Resource.Error -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
            else -> Unit
        }
    }

    fun onLocationSelected(result: LocationSearchResult, onComplete: () -> Unit) {

        viewModelScope.launch {
            locationRepository.addLocation(Coordinates(result.latitude, result.longitude), placeId = result.id)
            onComplete()
        }
    }
}