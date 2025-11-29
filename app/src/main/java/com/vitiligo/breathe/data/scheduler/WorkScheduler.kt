package com.vitiligo.breathe.data.scheduler

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.vitiligo.breathe.data.worker.DataSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    @ApplicationContext context: Context
) {
    private val HOURLY_SYNC_WORK_NAME = "HourlyDataSyncWork"

    private val workManager = WorkManager.getInstance(context)

    fun scheduleHourlySync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val hourlySyncRequest = PeriodicWorkRequest.Builder(
            DataSyncWorker::class.java,
            1,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            HOURLY_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            hourlySyncRequest
        )
    }

    fun cancelHourlySync() {
        workManager.cancelUniqueWork(HOURLY_SYNC_WORK_NAME)
    }
}
