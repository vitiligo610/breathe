package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.vitiligo.breathe.data.remote.model.LocationAirQualityData
import com.vitiligo.breathe.data.remote.model.LocationWeatherData

class LocationDetailsConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromWeatherData(value: LocationWeatherData?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWeatherData(value: String): LocationWeatherData? {
        return gson.fromJson(value, LocationWeatherData::class.java)
    }

    @TypeConverter
    fun fromAirQualityData(value: LocationAirQualityData?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAirQualityData(value: String): LocationAirQualityData? {
        return gson.fromJson(value, LocationAirQualityData::class.java)
    }
}