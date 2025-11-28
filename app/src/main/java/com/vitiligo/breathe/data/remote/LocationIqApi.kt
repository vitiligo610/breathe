package com.vitiligo.breathe.data.remote

import com.vitiligo.breathe.data.remote.model.locationiq.LocationIqResult
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationIqApi {

    @GET("search/structured")
    suspend fun searchLocations(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("accept-language") language: String = "en",
        @Query("tag") tag: String = "place:city,place:town,place:village"
    ): List<LocationIqResult>
}
