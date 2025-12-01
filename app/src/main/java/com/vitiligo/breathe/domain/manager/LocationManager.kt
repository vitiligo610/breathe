package com.vitiligo.breathe.domain.manager

import android.location.Location

interface LocationManager {

    suspend fun getCurrentLocation(): Location?
}
