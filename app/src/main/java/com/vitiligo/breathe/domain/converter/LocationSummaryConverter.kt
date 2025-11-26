package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.vitiligo.breathe.data.local.entity.DailyForecast
import kotlinx.serialization.json.Json

class LocationSummaryConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromForecastList(value: List<DailyForecast>?): String {
        return value?.let { json.encodeToString(it) } ?: "[]"
    }

    @TypeConverter
    fun toForecastList(value: String): List<DailyForecast>? {
        return if (value.isEmpty() || value == "[]") {
            emptyList()
        } else {
            try {
                json.decodeFromString(value)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}