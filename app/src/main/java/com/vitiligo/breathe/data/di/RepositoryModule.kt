package com.vitiligo.breathe.data.di

import com.vitiligo.breathe.data.repository.LocationSummaryRepositoryImpl
import com.vitiligo.breathe.domain.repository.LocationSummaryRepository
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
}