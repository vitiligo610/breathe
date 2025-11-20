package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitiligo.breathe.data.local.entity.SensorData
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDataDao {
    @Query("SELECT * FROM sensor_data WHERE location_id = :locationId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestSensorDataForLocation(locationId: Int): Flow<SensorData?>

    @Query("SELECT * FROM sensor_data WHERE location_id = :locationId ORDER BY timestamp DESC")
    fun getSensorHistoryForLocation(locationId: Int): Flow<List<SensorData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSensorData(sensorData: SensorData)
}
