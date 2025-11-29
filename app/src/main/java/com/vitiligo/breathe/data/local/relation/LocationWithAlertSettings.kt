package com.vitiligo.breathe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vitiligo.breathe.data.local.entity.LocationAlertSettings
import com.vitiligo.breathe.data.local.entity.UserLocation

data class LocationWithAlertSettings(
    @Embedded val location: UserLocation,

    @Relation(
        parentColumn = "id",
        entityColumn = "location_id"
    )
    val settings: LocationAlertSettings?
)
