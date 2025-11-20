package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitiligo.breathe.data.local.entity.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM weather_data WHERE location_id = :locationId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestWeatherDataForLocation(locationId: Int): Flow<WeatherData?>

    @Query("SELECT * FROM weather_data WHERE location_id = :locationId ORDER BY timestamp DESC")
    fun getWeatherHistoryForLocation(locationId: Int): Flow<List<WeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherData)
}