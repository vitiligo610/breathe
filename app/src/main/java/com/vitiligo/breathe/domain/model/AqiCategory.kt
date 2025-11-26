package com.vitiligo.breathe.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AqiCategory(val label: String) {
    @SerialName("green") GREEN("green"),
    @SerialName("yellow") YELLOW("yellow"),
    @SerialName("orange") ORANGE("orange"),
    @SerialName("red") RED("red"),
    @SerialName("purple") PURPLE("purple"),
    @SerialName("maroon") MAROON("maroon")
}
