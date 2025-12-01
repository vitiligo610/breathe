package com.vitiligo.breathe.data.manager

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.vitiligo.breathe.domain.manager.GeocodingManager
import com.vitiligo.breathe.domain.model.AddressInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class AndroidGeocodingManager @Inject constructor(
    @ApplicationContext context: Context
): GeocodingManager {

    private val geocoder = Geocoder(context, Locale.getDefault())

    override suspend fun reverseGeocode(latitude: Double, longitude: Double): AddressInfo? {
        return withContext(Dispatchers.IO) {
            try {
                @Suppress("DEPRECATION")
                val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    return@withContext addresses[0].toAddressInfo(latitude, longitude)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext null
        }
    }
}

private fun Address.toAddressInfo(lat: Double, lng: Double): AddressInfo {
    val city = locality ?: subAdminArea ?: "Unknown City"
    val country = countryName ?: "Unknown Country"

    return AddressInfo(
        latitude = lat,
        longitude = lng,
        city = city,
        country = country,
        postalCode = postalCode,
        thoroughfare = thoroughfare,
        featureName = featureName
    )
}