package com.vitiligo.breathe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vitiligo.breathe.domain.model.AqiCategory

@Entity(tableName = "locations")
data class Location (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
