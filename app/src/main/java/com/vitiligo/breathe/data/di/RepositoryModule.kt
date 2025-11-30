package com.vitiligo.breathe.data.di

import com.vitiligo.breathe.data.repository.LocationDetailsRepositoryImpl
import com.vitiligo.breathe.data.repository.LocationHistoryRepositoryImpl
import com.vitiligo.breathe.data.repository.LocationSearchRepositoryImpl
import com.vitiligo.breathe.data.repository.LocationSummaryRepositoryImpl
import com.vitiligo.breathe.data.repository.MapLocationRepositoryImpl
import com.vitiligo.breathe.domain.repository.LocationDetailsRepository
import com.vitiligo.breathe.domain.repository.LocationHistoryRepository
import com.vitiligo.breathe.domain.repository.LocationSearchRepository
import com.vitiligo.breathe.domain.repository.LocationSummaryRepository
import com.vitiligo.breathe.domain.repository.MapLocationRepository
import com.vitiligo.breathe.domain.util.HealthGuidanceEngine
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocationSummaryRepository(
        locationSummaryRepositoryImpl: LocationSummaryRepositoryImpl
    ): LocationSummaryRepository

    @Binds
    @Singleton
    abstract fun bindLocationDetailsRepository(
        locationDetailsRepositoryImpl: LocationDetailsRepositoryImpl
    ): LocationDetailsRepository

    @Binds
    @Singleton
    abstract fun bindLocationHistoryRepository(
        locationHistoryRepositoryImpl: LocationHistoryRepositoryImpl
    ): LocationHistoryRepository

    @Binds
    @Singleton
    abstract fun bindLocationSearchRepository(
        locationSearchRepositoryImpl: LocationSearchRepositoryImpl
    ): LocationSearchRepository

    @Binds
    @Singleton
    abstract fun bindMapLocationRepository(
        mapLocationRepositoryImpl: MapLocationRepositoryImpl
    ): MapLocationRepository
}
