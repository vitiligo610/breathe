package com.vitiligo.breathe.domain.model

data class Pollutant(
    val key: String,
    val displayValue: String,
    val description: String,
    val reading: Double,
    val category: AqiCategory,
    val unit: String = "µg/m³"
)
