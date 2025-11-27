package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vitiligo.breathe.data.local.entity.LocationHistory
import com.vitiligo.breathe.data.local.relation.LocationWithHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationHistoryDao {

    @Transaction
    @Query("SELECT * FROM user_locations WHERE id = :locationId")
    fun getLocationWithHistory(locationId: Int): Flow<LocationWithHistory?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationHistory(history: LocationHistory)
}