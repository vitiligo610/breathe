package com.vitiligo.breathe.data.repository

import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.data.remote.model.report.CreateReportRequest
import com.vitiligo.breathe.data.remote.model.report.PollutionReport
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.domain.repository.CommunityReportRepository
import com.vitiligo.breathe.domain.util.Resource
import javax.inject.Inject

class CommunityReportRepositoryImpl @Inject constructor(
    private val api: BreatheApi
): CommunityReportRepository {

    override suspend fun submitReport(
        latitude: Double,
        longitude: Double,
        reportType: ReportType,
        description: String
    ): Resource<PollutionReport> {
        val request = CreateReportRequest(
            latitude = latitude,
            longitude = longitude,
            reportType = reportType,
            description = description
        )

        try {
            val response = api.submitReport(request)

            return if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Failed to submit community report.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Failed to submit community report: ${e.localizedMessage}")
        }
    }
}