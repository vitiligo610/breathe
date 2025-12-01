package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.vitiligo.breathe.data.remote.model.report.ReportType

class ReportTypeConverter {

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
}