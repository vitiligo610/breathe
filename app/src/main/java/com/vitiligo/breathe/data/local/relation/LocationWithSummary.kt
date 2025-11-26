package com.vitiligo.breathe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vitiligo.breathe.data.local.entity.LocationSummary
import com.vitiligo.breathe.data.local.entity.UserLocation

data class LocationWithSummary(
    @Embedded val location: UserLocation,

    @Relation(
        parentColumn = "id",
        entityColumn = "locationId"
    )
    val summary: LocationSummary?
)
