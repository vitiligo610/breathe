package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.data.remote.model.report.PollutionReport
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.domain.util.Resource

interface CommunityReportRepository {

    suspend fun submitReport(
        latitude: Double,
        longitude: Double,
        reportType: ReportType,
        description: String
    ): Resource<PollutionReport>
}
