package com.vitiligo.breathe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vitiligo.breathe.domain.converter.LocationAlertSettingsConverters
import com.vitiligo.breathe.domain.model.health.HealthProfile
import com.vitiligo.breathe.domain.model.health.LocationLabel

@Entity(
    tableName = "location_alert_settings",
    foreignKeys = [
        ForeignKey(
            entity = UserLocation::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["location_id"], unique = true)]
)
@TypeConverters(LocationAlertSettingsConverters::class)
data class LocationAlertSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "location_id")
    val locationId: Int,

    val label: LocationLabel = LocationLabel.OTHER,

    @ColumnInfo(name = "health_profile")
    val healthProfile: HealthProfile = HealthProfile.GENERAL,

    @ColumnInfo(name = "is_notification_enabled")
    val isNotificationEnabled: Boolean = true,

    @ColumnInfo(name = "min_notification_interval_minutes")
    val minNotificationIntervalMinutes: Long = 60,

    @ColumnInfo(name = "last_notification_sent_timestamp")
    val lastNotificationSentTimestamp: Long = 0
)
