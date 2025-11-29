package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.data.local.entity.LocationAlertSettings
import com.vitiligo.breathe.data.local.entity.UserLocation

data class HealthZoneUiModel(
    val location: UserLocation,
    val settings: LocationAlertSettings?
)