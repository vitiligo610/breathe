package com.vitiligo.breathe.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.vitiligo.breathe.data.local.room.AppDatabase
import com.vitiligo.breathe.data.local.room.dao.AqiDataDao
import com.vitiligo.breathe.data.local.room.dao.LocationDao
import com.vitiligo.breathe.data.local.room.dao.LocationDetailsDao
import com.vitiligo.breathe.data.local.room.dao.LocationHistoryDao
import com.vitiligo.breathe.data.local.room.dao.LocationSummaryDao
import com.vitiligo.breathe.data.local.room.dao.SensorDataDao
import com.vitiligo.breathe.data.local.room.dao.UserLocationDao
import com.vitiligo.breathe.data.local.room.dao.WeatherDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "breathe_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: AppDatabase): LocationDao = database.locationDao()

    @Provides
    @Singleton
    fun provideAqiDataDao(database: AppDatabase): AqiDataDao = database.aqiDataDao()

    @Provides
    @Singleton
    fun provideWeatherDataDao(database: AppDatabase): WeatherDataDao = database.weatherDataDao()

    @Provides
    @Singleton
    fun provideSensorDataDao(database: AppDatabase): SensorDataDao = database.sensorDataDao()

    @Provides
    @Singleton
    fun provideUserLocationDao(database: AppDatabase): UserLocationDao = database.userLocationDao()

    @Provides
    @Singleton
    fun provideLocationSummaryDao(database: AppDatabase): LocationSummaryDao = database.locationSummaryDao()

    @Provides
    @Singleton
    fun provideLocationDetailsDao(database: AppDatabase): LocationDetailsDao = database.locationDetailsDao()

    @Provides
    @Singleton
    fun provideLocationHistoryDao(database: AppDatabase): LocationHistoryDao = database.locationHistoryDao()
}
