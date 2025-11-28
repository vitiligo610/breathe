package com.vitiligo.breathe.data.repository

import android.util.Log
import com.vitiligo.breathe.data.di.LocationIqApiKeyQualifier
import com.vitiligo.breathe.data.remote.LocationIqApi
import com.vitiligo.breathe.domain.model.LocationSearchResult
import com.vitiligo.breathe.domain.repository.LocationSearchRepository
import com.vitiligo.breathe.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationSearchRepositoryImpl @Inject constructor(
    @param:LocationIqApiKeyQualifier private val apiKey: String,
    private val api: LocationIqApi
) : LocationSearchRepository {

    override suspend fun search(query: String): Resource<List<LocationSearchResult>> {
        return withContext(Dispatchers.IO) {
            try {
                if (query.length < 2) return@withContext Resource.Success(emptyList())

                val results = api.searchLocations(
                    apiKey = apiKey,
                    query = query
                )

                Log.d("apii results are ", "$results")

                val domainResults = results.map { dto ->
                    val parts = listOfNotNull(dto.address?.city, dto.address?.state, dto.address?.country)
                        .filter { it.isNotBlank() && it != dto.address?.name }
                        .distinct()

                    LocationSearchResult(
                        id = dto.placeId,
                        name = dto.address?.name ?: dto.displayName.substringBefore(","),
                        region = parts.joinToString(", "),
                        latitude = dto.lat.toDoubleOrNull() ?: 0.0,
                        longitude = dto.lon.toDoubleOrNull() ?: 0.0,
                        countryCode = dto.address?.countryCode ?: ""
                    )
                }

                Log.d("results are ", "$domainResults")

                Resource.Success(domainResults)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Search failed: ${e.localizedMessage}")
            }
        }
    }
}