package com.vitiligo.breathe.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitiligo.breathe.data.local.entity.LocationAlertSettings
import com.vitiligo.breathe.data.local.room.dao.LocationAlertDao
import com.vitiligo.breathe.domain.model.health.HealthProfile
import com.vitiligo.breathe.domain.model.health.LocationLabel
import com.vitiligo.breathe.domain.model.ui.HealthZoneUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val dao: LocationAlertDao
) : ViewModel() {

    private val _locationsFlow = dao.getAllUserLocationsFlow()
    private val _settingsFlow = dao.getAllAlertSettingsFlow()

    val state: StateFlow<HealthScreenUiState> = combine(
        _locationsFlow,
        _settingsFlow
    ) { locations, settingsList ->

        val zones = locations.map { location ->
            val matchingSettings = settingsList.find { it.locationId == location.id }
            HealthZoneUiModel(location, matchingSettings)
        }

        HealthScreenUiState(zones = zones)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HealthScreenUiState(isLoading = true)
    )

    fun saveZoneSettings(
        locationId: Int,
        label: LocationLabel,
        profile: HealthProfile,
        enabled: Boolean,
        intervalMinutes: Long
    ) {
        viewModelScope.launch {
            val currentSettings = state.value.zones.find { it.location.id == locationId }?.settings

            val newSettings = LocationAlertSettings(
                id = currentSettings?.id ?: 0,
                locationId = locationId,
                label = label,
                healthProfile = profile,
                isNotificationEnabled = enabled,
                minNotificationIntervalMinutes = intervalMinutes,
                lastNotificationSentTimestamp = currentSettings?.lastNotificationSentTimestamp ?: 0
            )

            dao.insertAlertSettings(newSettings)
        }
    }

    fun toggleNotification(locationId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            val zone = state.value.zones.find { it.location.id == locationId } ?: return@launch
            val settings = zone.settings ?: return@launch

            dao.insertAlertSettings(settings.copy(isNotificationEnabled = isEnabled))
        }
    }
}