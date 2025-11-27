package com.vitiligo.breathe.data.remote

import com.vitiligo.breathe.data.remote.model.LocationClimateDetailsResponse
import com.vitiligo.breathe.data.remote.model.LocationClimateSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BreatheApi {

    @GET("api/location/summary")
    suspend fun getLocationSummary(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): LocationClimateSummaryResponse

    @GET("api/location")
    suspend fun getLocationDetails(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): LocationClimateDetailsResponse
}
