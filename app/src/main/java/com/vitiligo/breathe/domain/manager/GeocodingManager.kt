package com.vitiligo.breathe.domain.manager

import com.vitiligo.breathe.domain.model.AddressInfo

interface GeocodingManager {

    suspend fun reverseGeocode(latitude: Double, longitude: Double): AddressInfo?
}
