package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.vitiligo.breathe.domain.model.health.HealthProfile
import com.vitiligo.breathe.domain.model.health.LocationLabel

class LocationAlertSettingsConverters {

    @TypeConverter
    fun fromLocationLabel(value: LocationLabel): String {
        return value.name
    }

    @TypeConverter
    fun toLocationLabel(value: String?): LocationLabel {
        return try {
            if (value.isNullOrEmpty()) {
                LocationLabel.OTHER
            } else {
                LocationLabel.valueOf(value)
            }
        } catch (e: IllegalArgumentException) {
            LocationLabel.OTHER
        }
    }

    @TypeConverter
    fun fromHealthProfile(value: HealthProfile): String {
        return value.name
    }

    @TypeConverter
    fun toHealthProfile(value: String?): HealthProfile {
        return try {
            if (value.isNullOrEmpty()) {
                HealthProfile.GENERAL
            } else {
                HealthProfile.valueOf(value)
            }
        } catch (e: IllegalArgumentException) {
            HealthProfile.GENERAL
        }
    }
}