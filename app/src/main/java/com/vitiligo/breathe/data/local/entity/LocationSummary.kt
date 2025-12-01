package com.vitiligo.breathe.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vitiligo.breathe.data.remote.model.report.PollutionReport
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.domain.converter.LocationSummaryConverter
import com.vitiligo.breathe.domain.converter.ReportTypeConverter
import com.vitiligo.breathe.domain.model.AqiCategory
import kotlinx.serialization.Serializable

@Entity(
    tableName = "location_summaries",
    foreignKeys = [
        ForeignKey(
            entity = UserLocation::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["locationId"], unique = true)]
)
@TypeConverters(
    LocationSummaryConverter::class,
    ReportTypeConverter::class
)
data class LocationSummary(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val locationId: Int,

    val lastUpdatedTimestamp: Long,

    val currentTemp: Double,
    val currentAqi: Int,
    val currentWeatherCode: Int,

    val dailyForecasts: List<DailyForecast>,
    val recentReportType: ReportType? = null
)

@Serializable
data class DailyForecast(
    val dayLabel: String,
    val aqi: Int,
    val aqiCategory: AqiCategory,
    val weatherCode: Int,
    val highTempC: Int,
    val lowTempC: Int
)