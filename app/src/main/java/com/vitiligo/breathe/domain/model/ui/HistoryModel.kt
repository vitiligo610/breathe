package com.vitiligo.breathe.domain.model.ui

import com.vitiligo.breathe.domain.model.AqiCategory
import java.time.LocalDateTime

enum class HistoryViewMode {
    Hourly,
    Daily
}

enum class HistoryTabOption(val key: String, val label: String, val unit: String) {
    AQI("aqi", "AQI+", "US"),
    PM2_5("pm2_5", "PM2.5", "(μg/m³)"),
    PM10("pm10", "PM10", "(μg/m³)"),
    O3("o3", "O3", "(μg/m³)"),
    NO2("no2", "NO2", "(μg/m³)"),
    SO2("so2", "SO2", "(μg/m³)"),
    CO("co", "CO", "(μg/m³)")
}

data class HistoryPoint(
    val timestamp: LocalDateTime,
    val value: Double,
    val category: AqiCategory = AqiCategory.GREEN
)
