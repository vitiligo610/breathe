package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vitiligo.breathe.data.local.entity.LocationDetails
import com.vitiligo.breathe.data.local.relation.LocationWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDetailsDao {

    @Transaction
    @Query("SELECT * FROM user_locations WHERE id = :locationId")
    fun getLocationDetailsWithLocation(locationId: Int): Flow<LocationWithDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationDetails(details: LocationDetails)

    @Query("SELECT lastUpdatedTimestamp FROM location_details WHERE locationId = :locationId")
    suspend fun getLastUpdatedTime(locationId: Int): Long?
}