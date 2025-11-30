package com.vitiligo.breathe.ui.map

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import com.vitiligo.breathe.domain.repository.MapLocationRepository
import com.vitiligo.breathe.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapLocationRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    var state = mutableStateOf(MapUiState())

    private var fetchJob: Job? = null

    fun onMarkerSelected(aqi: Int, lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            var cityName = "Unknown Location"
            var countryName = "Unknown Country"

            try {
                val geocoder = Geocoder(context, Locale.getDefault())

                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]

                    cityName = address.locality ?: address.subAdminArea ?: "Unknown City"
                    countryName = address.countryName ?: "Unknown Country"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val selectedInfo = SelectedPointInfo(
                latitude = lat,
                longitude = lng,
                city = cityName,
                country = countryName,
                aqi = aqi
            )

            withContext(Dispatchers.Main) {
                state.value = state.value.copy(
                    selectedPoint = selectedInfo
                )
            }
        }
    }

    fun onMarkerDismissed() {
        state.value = state.value.copy(selectedPoint = null)
    }

    fun onCameraIdle(
        swLat: Double, swLon: Double,
        neLat: Double, neLon: Double,
        zoom: Double
    ) {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            delay(350)

            val gridResolution = when {
                zoom < 5 -> 8
                zoom < 8 -> 12
                zoom < 11 -> 20
                else -> 30
            }

            val result = repository.getMarkers(
                swLat, swLon, neLat, neLon, gridResolution
            )

            if (result is Resource.Success) {
                state.value = state.value.copy(mapPoints = result.data ?: emptyList())
            }
        }
    }
}