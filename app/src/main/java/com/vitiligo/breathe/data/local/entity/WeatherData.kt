package com.vitiligo.breathe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weather_data",
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
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "location_id")
    val locationId: Int,

    @ColumnInfo(name = "temperature_c")
    val temperatureC: Double,

    @ColumnInfo(name = "humidity_percent")
    val humidityPercent: Double,

    @ColumnInfo(name = "weather_icon")
    val weatherIcon: String,

    @ColumnInfo(name = "weather_description")
    val weatherDescription: String,

    @ColumnInfo(name = "wind_speed_mps")
    val windSpeedMps: Double,

    @ColumnInfo(name = "wind_angle")
    val windAngle: Double,

    val timestamp: Long
)