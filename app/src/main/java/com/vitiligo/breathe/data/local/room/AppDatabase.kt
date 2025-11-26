package com.vitiligo.breathe.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vitiligo.breathe.data.local.entity.AqiData
import com.vitiligo.breathe.data.local.entity.Location
import com.vitiligo.breathe.data.local.entity.LocationSummary
import com.vitiligo.breathe.data.local.entity.SensorData
import com.vitiligo.breathe.data.local.entity.UserLocation
import com.vitiligo.breathe.data.local.entity.WeatherData
import com.vitiligo.breathe.data.local.room.dao.AqiDataDao
import com.vitiligo.breathe.data.local.room.dao.LocationDao
import com.vitiligo.breathe.data.local.room.dao.LocationSummaryDao
import com.vitiligo.breathe.data.local.room.dao.SensorDataDao
import com.vitiligo.breathe.data.local.room.dao.WeatherDataDao

@Database(
    entities = [
        Location::class,
        AqiData::class,
        WeatherData::class,
        SensorData::class,
        UserLocation::class,
        LocationSummary::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun aqiDataDao(): AqiDataDao
    abstract fun weatherDataDao(): WeatherDataDao
    abstract fun sensorDataDao(): SensorDataDao
    abstract fun locationSummaryDao(): LocationSummaryDao
}