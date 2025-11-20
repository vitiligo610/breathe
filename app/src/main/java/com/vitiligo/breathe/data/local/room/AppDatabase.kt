package com.vitiligo.breathe.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vitiligo.breathe.data.local.entity.AqiData
import com.vitiligo.breathe.data.local.entity.Location
import com.vitiligo.breathe.data.local.entity.SensorData
import com.vitiligo.breathe.data.local.entity.WeatherData
import com.vitiligo.breathe.data.local.room.dao.AqiDataDao
import com.vitiligo.breathe.data.local.room.dao.LocationDao
import com.vitiligo.breathe.data.local.room.dao.SensorDataDao
import com.vitiligo.breathe.data.local.room.dao.WeatherDataDao
import com.vitiligo.breathe.domain.converter.AqiCategoryConverter

@Database(
    entities = [
        Location::class,
        AqiData::class,
        WeatherData::class,
        SensorData::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(AqiCategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun aqiDataDao(): AqiDataDao
    abstract fun weatherDataDao(): WeatherDataDao
    abstract fun sensorDataDao(): SensorDataDao
}