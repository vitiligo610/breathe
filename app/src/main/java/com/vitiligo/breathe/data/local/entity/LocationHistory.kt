package com.vitiligo.breathe.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vitiligo.breathe.data.remote.model.HistoryForecast
import com.vitiligo.breathe.domain.converter.LocationHistoryConverters

@Entity(
    tableName = "location_history",
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
@TypeConverters(LocationHistoryConverters::class)
data class LocationHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locationId: Int,
    val lastUpdatedTimestamp: Long,

    val hourlyForecast: HistoryForecast,
    val dailyForecast: HistoryForecast
)
