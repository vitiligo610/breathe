package com.vitiligo.breathe.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vitiligo.breathe.data.remote.model.LocationAirQualityData
import com.vitiligo.breathe.data.remote.model.LocationWeatherData
import com.vitiligo.breathe.domain.converter.LocationDetailsConverters

@Entity(
    tableName = "location_details",
    foreignKeys = [
        ForeignKey(
            entity = UserLocation::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["locationId"], unique = true)]
)
@TypeConverters(LocationDetailsConverters::class)
data class LocationDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locationId: Int,
    val lastUpdatedTimestamp: Long,

    val weatherData: LocationWeatherData,
    val airQualityData: LocationAirQualityData
)
