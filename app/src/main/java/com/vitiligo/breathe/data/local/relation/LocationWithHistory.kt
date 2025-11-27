package com.vitiligo.breathe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vitiligo.breathe.data.local.entity.LocationHistory
import com.vitiligo.breathe.data.local.entity.UserLocation

data class LocationWithHistory(
    @Embedded val location: UserLocation,

    @Relation(
        parentColumn = "id",
        entityColumn = "locationId"
    )
    val history: LocationHistory?
)