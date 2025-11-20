package com.vitiligo.breathe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sensor_data",
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
data class SensorData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "location_id")
    val locationId: Int,

    @ColumnInfo(name = "node_id")
    val nodeId: String,

    val timestamp: Long,
    val temperature: Double,
    val humidity: Double,

    @ColumnInfo(name = "mq4_ch4")
    val mq4Ch4: Double,

    @ColumnInfo(name = "mq7_co")
    val mq7Co: Double,

    @ColumnInfo(name = "mq135_air")
    val mq135Air: Double,

    @ColumnInfo(name = "dust_ugm3")
    val dustUgm3: Double
)