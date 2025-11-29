package com.vitiligo.breathe.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vitiligo.breathe.data.local.room.dao.LocationAlertDao
import com.vitiligo.breathe.data.local.room.dao.LocationSummaryDao
import com.vitiligo.breathe.data.mapper.toSummaryEntity
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.domain.model.health.AlertSeverity
import com.vitiligo.breathe.domain.util.HealthGuidanceEngine
import com.vitiligo.breathe.presentation.notification.BreatheNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val api: BreatheApi,
    private val alertDao: LocationAlertDao,
    private val summaryDao: LocationSummaryDao,
    private val healthEngine: HealthGuidanceEngine,
    private val notificationManager: BreatheNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val locations = alertDao.getLocationsWithSettings()

            locations.forEach { item ->
                val location = item.location
                val settings = item.settings

                val summaryDto = api.getLocationSummary(location.latitude, location.longitude)

                val summaryEntity = summaryDto.toSummaryEntity(location.id)
                summaryDao.insertLocationSummary(summaryEntity)

                if (settings != null && settings.isNotificationEnabled) {
                    val currentAqi = summaryDto.current?.aqi ?: 0

                    val timeSinceLast = System.currentTimeMillis() - settings.lastNotificationSentTimestamp
                    val minIntervalMillis = TimeUnit.MINUTES.toMillis(settings.minNotificationIntervalMinutes)

                    if (timeSinceLast > minIntervalMillis) {

                        val guidance = healthEngine.analyze(
                            currentAqi = currentAqi,
                            dominantPollutant = "PM2.5",
                            profile = settings.healthProfile
                        )

                        if (guidance.severity == AlertSeverity.WARNING || guidance.severity == AlertSeverity.EMERGENCY) {

                            notificationManager.sendHealthAlert(
                                notificationId = location.id,
                                title = guidance.title,
                                message = guidance.actionableMessage,
                                locationName = "${settings.label.display} (${location.name})"
                            )

                            val updatedSettings = settings.copy(lastNotificationSentTimestamp = System.currentTimeMillis())
                            alertDao.insertAlertSettings(updatedSettings)
                        }
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}