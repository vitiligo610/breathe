package com.vitiligo.breathe.data.remote

import com.vitiligo.breathe.data.remote.model.LocationAirQualityHistoryResponse
import com.vitiligo.breathe.data.remote.model.LocationClimateDetailsResponse
import com.vitiligo.breathe.data.remote.model.LocationClimateSummaryResponse
import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import com.vitiligo.breathe.data.remote.model.report.CreateReportRequest
import com.vitiligo.breathe.data.remote.model.report.PollutionReport
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BreatheApi {

    @GET("api/location/summary")
    suspend fun getLocationSummary(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("reports_radius_km") reportsRadiusKm: Double = 50.0
    ): LocationClimateSummaryResponse

    @GET("api/location")
    suspend fun getLocationDetails(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("reports_radius_km") reportsRadiusKm: Double = 50.0
    ): LocationClimateDetailsResponse

    @GET("api/location/history")
    suspend fun getLocationHistory(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): LocationAirQualityHistoryResponse

    @GET("api/location/map")
    suspend fun getLocationMap(
        @Query("bounding_box") boundingBox: String,
        @Query("grid_resolution") gridResolution: Int,
        @Query("marker_type") markerType: String
    ): List<MapLocationPoint>

    @POST("api/location/report")
    suspend fun submitReport(
        @Body request: CreateReportRequest
    ): Response<PollutionReport>
}
