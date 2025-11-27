package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.vitiligo.breathe.data.remote.model.HistoryForecast

class LocationHistoryConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromHistoryForecast(value: HistoryForecast?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toHistoryForecast(value: String): HistoryForecast? {
        return gson.fromJson(value, HistoryForecast::class.java)
    }
}