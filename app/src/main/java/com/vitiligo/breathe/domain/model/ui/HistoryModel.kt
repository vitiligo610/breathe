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
    O3("o3", "O3", "(ppb)"),
    NO2("no2", "NO2", "(ppb)"),
    SO2("so2", "SO2", "(ppb)"),
    CO("co", "CO", "(ppm)")
}

data class HistoryPoint(
    val timestamp: LocalDateTime,
    val value: Double,
) {
    fun getCategory(): AqiCategory {
        val score = value.toInt()
        return when {
            score <= 50 -> AqiCategory.GREEN
            score <= 100 -> AqiCategory.YELLOW
            score <= 150 -> AqiCategory.ORANGE
            score <= 200 -> AqiCategory.RED
            score <= 300 -> AqiCategory.PURPLE
            else -> AqiCategory.MAROON
        }
    }
}
