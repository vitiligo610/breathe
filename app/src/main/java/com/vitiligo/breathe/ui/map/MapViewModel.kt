package com.vitiligo.breathe.ui.map

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.vitiligo.breathe.domain.model.LocationSearchResult
import com.vitiligo.breathe.domain.repository.LocationSearchRepository
import com.vitiligo.breathe.domain.repository.MapLocationRepository
import com.vitiligo.breathe.domain.util.LocationManager
import com.vitiligo.breathe.domain.util.Resource
import com.vitiligo.breathe.ui.search.LocationSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.maplibre.android.geometry.LatLng
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapLocationRepository,
    private val searchRepository: LocationSearchRepository,
    private val locationManager: LocationManager,
    @param:ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState = mutableStateOf(MapUiState())
        private set

    var searchState = mutableStateOf(LocationSearchUiState())
        private set

    @OptIn(SavedStateHandleSaveableApi::class)
    var cameraState by savedStateHandle.saveable(MAP_STATE_KEY) {
        mutableStateOf(
            MapCameraState(
                latitude = DEFAULT_LOCATION_LAT,
                longitude = DEFAULT_LOCATION_LON,
                zoom = DEFAULT_ZOOM
            )
        )
    }

    private var searchJob: Job? = null
    private var fetchJob: Job? = null

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
                uiState.value = uiState.value.copy(mapPoints = result.data ?: emptyList())
            }
        }
    }

    fun updateCameraState(lat: Double?, lon: Double?, zoom: Double) {
        cameraState = MapCameraState(lat ?: DEFAULT_LOCATION_LAT, lon ?: DEFAULT_LOCATION_LON, zoom)
    }

    fun getCurrentLocation(onLocationFound: suspend (LatLng?) -> Unit) {
        viewModelScope.launch {
            val location = locationManager.getCurrentLocation()

            if (location != null) {
                onLocationFound(LatLng(location.latitude, location.longitude))
            } else {
                onLocationFound(null)
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchState.value = searchState.value.copy(query = newQuery)

        searchJob?.cancel()

        if (newQuery.isBlank()) {
            searchState.value = searchState.value.copy(results = emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            searchState.value = searchState.value.copy(isLoading = true)

            when (val result = searchRepository.search(newQuery)) {
                is Resource.Success -> {
                    searchState.value = searchState.value.copy(
                        results = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    searchState.value = searchState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> Unit
            }
        }
    }

    fun onSearchResultSelected(result: LocationSearchResult) {
        searchState.value = LocationSearchUiState()

        updateCameraState(result.latitude, result.longitude, 12.0)

        onCameraIdle(
            result.latitude - 0.1, result.longitude - 0.1,
            result.latitude + 0.1, result.longitude + 0.1,
            12.0
        )
    }

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
                uiState.value = uiState.value.copy(
                    selectedPoint = selectedInfo
                )
            }
        }
    }

    fun onMarkerDismissed() {
        uiState.value = uiState.value.copy(selectedPoint = null)
    }

    companion object {
        private const val MAP_STATE_KEY = "map_camera_state"
        private const val DEFAULT_LOCATION_LAT = 30.3753
        private const val DEFAULT_LOCATION_LON = 69.5421
        private const val DEFAULT_ZOOM = 5.0
    }

}