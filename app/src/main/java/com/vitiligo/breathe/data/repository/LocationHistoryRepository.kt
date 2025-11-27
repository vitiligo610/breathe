package com.vitiligo.breathe.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.vitiligo.breathe.data.local.relation.LocationWithHistory
import com.vitiligo.breathe.data.local.room.dao.LocationHistoryDao
import com.vitiligo.breathe.data.local.room.dao.LocationSummaryDao
import com.vitiligo.breathe.data.local.room.dao.UserLocationDao
import com.vitiligo.breathe.data.mapper.toHistoryEntity
import com.vitiligo.breathe.data.remote.BreatheApi
import com.vitiligo.breathe.domain.repository.LocationHistoryRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationHistoryRepositoryImpl @Inject constructor(
    private val api: BreatheApi,
    private val historyDao: LocationHistoryDao,
    private val summaryDao: UserLocationDao
) : LocationHistoryRepository {

    override fun getLocationHistory(locationId: Int): Flow<LocationWithHistory?> {
        return historyDao.getLocationWithHistory(locationId)
    }

    override suspend fun refreshHistory(locationId: Int): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val target = summaryDao.getLocationById(locationId)
                    ?: return@withContext Resource.Error("Location not found")

                val dto = api.getLocationHistory(target.latitude, target.longitude)

                historyDao.insertLocationHistory(dto.toHistoryEntity(locationId))

                Resource.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.localizedMessage ?: "Failed to fetch history")
            }
        }
    }
}