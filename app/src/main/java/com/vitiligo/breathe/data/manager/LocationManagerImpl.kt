package com.vitiligo.breathe.data.manager

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.vitiligo.breathe.domain.manager.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManagerImpl @Inject constructor(
    @param:ApplicationContext val context: Context
): LocationManager {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { cont ->
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            cont.resume(location)
        }.addOnFailureListener { exception ->
            cont.resumeWithException(exception)
        }.addOnCanceledListener {
            cont.cancel()
        }

        cont.invokeOnCancellation {
            cancellationTokenSource.cancel()
        }
    }
}