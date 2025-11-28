package com.vitiligo.breathe.data.local.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.vitiligo.breathe.data.local.entity.AqiData
import com.vitiligo.breathe.data.local.entity.Location
import com.vitiligo.breathe.data.local.entity.LocationDetails
import com.vitiligo.breathe.data.local.entity.LocationHistory
import com.vitiligo.breathe.data.local.entity.LocationSummary
import com.vitiligo.breathe.data.local.entity.SensorData
import com.vitiligo.breathe.data.local.entity.UserLocation
import com.vitiligo.breathe.data.local.entity.WeatherData
import com.vitiligo.breathe.data.local.room.dao.AqiDataDao
import com.vitiligo.breathe.data.local.room.dao.LocationDao
import com.vitiligo.breathe.data.local.room.dao.LocationDetailsDao
import com.vitiligo.breathe.data.local.room.dao.LocationHistoryDao
import com.vitiligo.breathe.data.local.room.dao.LocationSummaryDao
import com.vitiligo.breathe.data.local.room.dao.SensorDataDao
import com.vitiligo.breathe.data.local.room.dao.UserLocationDao
import com.vitiligo.breathe.data.local.room.dao.WeatherDataDao

@Database(
    entities = [
        Location::class,
        AqiData::class,
        WeatherData::class,
        SensorData::class,
        UserLocation::class,
        LocationSummary::class,
        LocationDetails::class,
        LocationHistory::class
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun aqiDataDao(): AqiDataDao
    abstract fun weatherDataDao(): WeatherDataDao
    abstract fun sensorDataDao(): SensorDataDao
    abstract fun userLocationDao(): UserLocationDao
    abstract fun locationSummaryDao(): LocationSummaryDao
    abstract fun locationDetailsDao(): LocationDetailsDao
    abstract fun locationHistoryDao(): LocationHistoryDao
}