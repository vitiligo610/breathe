package com.vitiligo.breathe.domain.model.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    companion object {
        fun fromRoute(route: String): Screen? {
            return Screen::class.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }?.objectInstance
        }
    }

    @Serializable
    data object Home: Screen()

    @Serializable
    data class LocationDetails(
        val id: Int? = null,
        val coordinates: String? = null,
        val placeId: String? = null
    ): Screen()

    @Serializable
    data object LocationSearch: Screen()

    @Serializable
    data object Map: Screen()

    @Serializable
    data object Health: Screen()

    @Serializable
    data class CommunityReport(
        val latitude: Double,
        val longitude: Double
    ): Screen()
}
