package com.vitiligo.breathe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vitiligo.breathe.domain.model.AqiCategory

@Entity(
    tableName = "aqi_data",
    foreignKeys = [
        ForeignKey(
            entity = Location::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["location_id"])]
)
data class AqiData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "location_id")
    val locationId: Int,

    val aqi: Int,
    @ColumnInfo(name = "aqi_category")

    val aqiCategory: AqiCategory,
    val pm2_5: Double = 0.0,
    val pm10: Double = 0.0,
    val co: Double = 0.0,
    val so2: Double = 0.0,
    val no2: Double = 0.0,
    val o3: Double = 0.0,

    @ColumnInfo(name = "dominant_pollutant")
    val dominantPollutant: String,

    val timestamp: Long
)