package com.vitiligo.breathe.domain.util

import com.vitiligo.breathe.domain.model.health.AlertSeverity
import com.vitiligo.breathe.domain.model.health.HealthGuidance
import com.vitiligo.breathe.domain.model.health.HealthProfile
import jakarta.inject.Inject

class HealthGuidanceEngine @Inject constructor() {

    fun analyze(
        currentAqi: Int,
        dominantPollutant: String,
        profile: HealthProfile
    ): HealthGuidance {

        val effectiveAqi = (currentAqi / profile.sensitivityMultiplier).toInt()

        val severity = when {
            effectiveAqi > 300 -> AlertSeverity.EMERGENCY
            effectiveAqi > 200 -> AlertSeverity.WARNING
            effectiveAqi > 150 -> AlertSeverity.WARNING
            effectiveAqi > 100 -> AlertSeverity.NOTICE
            else -> AlertSeverity.NONE
        }

        if (severity == AlertSeverity.NONE) {
            return HealthGuidance(AlertSeverity.NONE, "", "")
        }

        val message = when (profile) {
            HealthProfile.ASTHMA -> {
                if (severity == AlertSeverity.EMERGENCY) "Dangerous air quality. Keep windows closed and inhaler ready. Avoid outdoors."
                else "Air quality is dropping. Limit outdoor exertion and keep medication nearby."
            }
            HealthProfile.COPD -> {
                if (severity == AlertSeverity.EMERGENCY) "Hazardous conditions. Stay indoors with filtered air."
                else "Irritants detected. Reduced lung function possible. Avoid strenuous activity."
            }
            HealthProfile.CHILD, HealthProfile.PREGNANCY, HealthProfile.ELDERLY -> {
                "Sensitive groups should reduce outdoor play or exercise."
            }
            HealthProfile.ATHLETE -> {
                "Consider moving training indoors or reducing intensity."
            }
            HealthProfile.GENERAL -> {
                "Air quality is unhealthy. Sensitive individuals should stay indoors."
            }
        }

        val title = "${profile.display} Alert: ${getLocationRiskLabel(effectiveAqi)}"

        return HealthGuidance(severity, title, message)
    }

    private fun getLocationRiskLabel(aqi: Int): String {
        return when {
            aqi > 300 -> "Hazardous"
            aqi > 200 -> "Very Unhealthy"
            aqi > 150 -> "Unhealthy"
            aqi > 100 -> "Moderate Risk"
            else -> "Good"
        }
    }
}