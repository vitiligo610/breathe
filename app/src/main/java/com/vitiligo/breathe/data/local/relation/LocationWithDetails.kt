package com.vitiligo.breathe.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vitiligo.breathe.data.local.entity.LocationDetails
import com.vitiligo.breathe.data.local.entity.UserLocation

data class LocationWithDetails(
    @Embedded val location: UserLocation,

    @Relation(
        parentColumn = "id",
        entityColumn = "locationId"
    )
    val details: LocationDetails?
)
