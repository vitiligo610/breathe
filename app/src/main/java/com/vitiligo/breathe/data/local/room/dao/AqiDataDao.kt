package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitiligo.breathe.data.local.entity.AqiData
import kotlinx.coroutines.flow.Flow

@Dao
interface AqiDataDao {
    @Query("SELECT * FROM aqi_data WHERE location_id = :locationId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestAqiDataForLocation(locationId: Int): Flow<AqiData?>

    @Query("SELECT * FROM aqi_data WHERE location_id = :locationId ORDER BY timestamp DESC")
    fun getAqiHistoryForLocation(locationId: Int): Flow<List<AqiData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAqiData(aqiData: AqiData)
}