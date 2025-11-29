package com.vitiligo.breathe.domain.model.health

enum class HealthProfile(val display: String, val sensitivityMultiplier: Double) {
    GENERAL("General Public", 1.0),
    ASTHMA("Asthmatic", 0.7),
    COPD("COPD", 0.6),
    ELDERLY("Elderly", 0.8),
    CHILD("Child", 0.8),
    PREGNANCY("Pregnancy", 0.8),
    ATHLETE("Outdoor Athlete", 0.9)
}
