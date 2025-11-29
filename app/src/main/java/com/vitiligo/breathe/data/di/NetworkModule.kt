package com.vitiligo.breathe.data.di

import com.vitiligo.breathe.BuildConfig
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.data.remote.LocationIqApi
import com.vitiligo.breathe.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @BreatheApiTenantSecretQualifier
    fun provideBreatheTenantToken(): String {
        return BuildConfig.TENANT_SECRET_TOKEN
    }

    @Provides
    @BreatheApiQualifier
    fun provideBreatheBaseUrl(): String {
        return BuildConfig.BREATHE_API_BASE_URL
    }

    @Provides
    @Singleton
    @LocationIqApiKeyQualifier
    fun provideLocationIqApiKey(): String {
        return BuildConfig.LOCATION_IQ_API_KEY
    }

    @Provides
    @LocationIqApiQualifier
    fun provideLocationIqBaseUrl(): String {
        return "https://us1.locationiq.com/v1/"
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        @BreatheApiTenantSecretQualifier tenantSecret: String
    ): Interceptor {
        return AuthInterceptor(tenantSecret)
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    @Provides
    @Singleton
    @BreatheApiQualifier
    fun provideBreatheOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @LocationIqApiQualifier
    fun provideLocationIqOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    @BreatheApiQualifier
    fun provideBreatheRetrofit(
        @BreatheApiQualifier baseUrl: String,
        @BreatheApiQualifier okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @LocationIqApiQualifier
    fun provideLocationIqRetrofit(
        @LocationIqApiQualifier baseUrl: String,
        @LocationIqApiQualifier okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBreatheApi(
        @BreatheApiQualifier retrofit: Retrofit
    ): BreatheApi {
        return retrofit.create(BreatheApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationIqApi(
        @LocationIqApiQualifier retrofit: Retrofit
    ): LocationIqApi {
        return retrofit.create(LocationIqApi::class.java)
    }
}