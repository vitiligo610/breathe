package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitiligo.breathe.data.local.entity.UserLocation

@Dao
interface UserLocationDao {

    @Query("SELECT * FROM user_locations WHERE id = :locationId")
    suspend fun getLocationById(locationId: Int): UserLocation?

    @Query("SELECT * FROM user_locations")
    suspend fun getAllUserLocations(): List<UserLocation>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserLocation(location: UserLocation): Long

    @Query("DELETE FROM user_locations WHERE id = :locationId")
    suspend fun deleteUserLocationById(locationId: Int)
}