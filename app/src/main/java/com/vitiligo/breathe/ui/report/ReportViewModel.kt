package com.vitiligo.breathe.ui.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.domain.manager.GeocodingManager
import com.vitiligo.breathe.domain.model.navigation.Screen
import com.vitiligo.breathe.domain.repository.CommunityReportRepository
import com.vitiligo.breathe.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: CommunityReportRepository,
    private val geocodingManager: GeocodingManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val screen = savedStateHandle.toRoute<Screen.CommunityReport>()
    private val latitude = checkNotNull(screen.latitude)
    private val longitude = checkNotNull(screen.longitude)

    private val _uiState = MutableStateFlow(ReportUiState(latitude = latitude, longitude = longitude))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchCoordinatesInfo()
        }
    }

    fun onTypeChange(type: ReportType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onDescriptionChange(text: String) {
        _uiState.update { it.copy(description = text) }
    }

    fun submitReport() {
        val currentState = _uiState.value
        if (currentState.description.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = repository.submitReport(
                latitude, longitude,
                currentState.selectedType,
                currentState.description
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    submitResult = result is Resource.Success
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ReportUiState(latitude = latitude, longitude = longitude)
    }

    private suspend fun fetchCoordinatesInfo() {
        val address = geocodingManager.reverseGeocode(latitude, longitude)

        if (address != null) {
            _uiState.update {
                it.copy(
                    city = address.city,
                    country = address.country
                )
            }
        }
    }
}