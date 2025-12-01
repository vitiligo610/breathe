package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.vitiligo.breathe.data.remote.model.report.PollutionReport
import com.vitiligo.breathe.data.remote.model.report.ReportType
import kotlinx.serialization.json.Json

class PollutionReportTypeConverter {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromReportType(type: ReportType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toReportType(name: String?): ReportType? {
        return name?.let {
            try {
                ReportType.valueOf(it)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }

    @TypeConverter
    fun fromPollutionReportList(value: List<PollutionReport>?): String {
        return value?.let { json.encodeToString(it) } ?: "[]"
    }

    @TypeConverter
    fun toPollutionReportList(value: String): List<PollutionReport> {
        return if (value.isEmpty() || value == "[]") {
            emptyList()
        } else {
            try {
                json.decodeFromString<List<PollutionReport>>(value)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
