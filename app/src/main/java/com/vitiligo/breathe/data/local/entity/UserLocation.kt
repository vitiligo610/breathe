package com.vitiligo.breathe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_locations")
data class UserLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val country: String,
    val timezone: String,
    val utcOffsetSeconds: Int
)
