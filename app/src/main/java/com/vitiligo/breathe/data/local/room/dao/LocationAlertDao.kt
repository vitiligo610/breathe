package com.vitiligo.breathe.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vitiligo.breathe.data.local.entity.LocationAlertSettings
import com.vitiligo.breathe.data.local.relation.LocationWithAlertSettings

@Dao
interface LocationAlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertSettings(settings: LocationAlertSettings)

    @Query("SELECT * FROM location_alert_settings WHERE location_id = :locationId")
    suspend fun getAlertSettings(locationId: Int): LocationAlertSettings?

    @Query("SELECT * FROM location_alert_settings")
    suspend fun getAllAlertSettings(): List<LocationAlertSettings>

    @Transaction
    @Query("SELECT * FROM user_locations")
    suspend fun getLocationsWithSettings(): List<LocationWithAlertSettings>
}