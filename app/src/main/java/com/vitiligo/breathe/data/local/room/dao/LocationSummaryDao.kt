package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vitiligo.breathe.data.local.entity.LocationSummary
import com.vitiligo.breathe.data.local.entity.UserLocation
import com.vitiligo.breathe.data.local.relation.LocationWithSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationSummaryDao {

    @Query("SELECT * FROM user_locations")
    suspend fun getAllUserLocations(): List<UserLocation>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserLocation(location: UserLocation): Long

    @Transaction
    @Query("SELECT * FROM user_locations")
    fun getLocationsWithSummary(): Flow<List<LocationWithSummary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationSummary(summary: LocationSummary)

    @Query("DELETE FROM location_summaries WHERE locationId = :locationId")
    suspend fun deleteSummaryByLocationId(locationId: Int)

    @Transaction
    suspend fun updateSummaries(summaries: List<LocationSummary>) {
        summaries.forEach { summary ->
            insertLocationSummary(summary)
        }
    }
}