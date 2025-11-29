package com.vitiligo.breathe.domain.model.health

data class HealthGuidance(
    val severity: AlertSeverity,
    val title: String,
    val actionableMessage: String
)
