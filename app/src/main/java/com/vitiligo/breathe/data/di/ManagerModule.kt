package com.vitiligo.breathe.data.di

import com.vitiligo.breathe.data.manager.AndroidGeocodingManager
import com.vitiligo.breathe.data.manager.LocationManagerImpl
import com.vitiligo.breathe.domain.manager.GeocodingManager
import com.vitiligo.breathe.domain.manager.LocationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    abstract fun bindGeocodingManager(
        androidGeocodingManager: AndroidGeocodingManager
    ): GeocodingManager

    @Binds
    abstract fun binidLocationManager(
        locationManagerImpl: LocationManagerImpl
    ): LocationManager
}