package com.vitiligo.breathe.presentation.health

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.domain.model.health.HealthProfile
import com.vitiligo.breathe.domain.model.health.LocationLabel
import com.vitiligo.breathe.domain.model.ui.HealthZoneUiModel

@Composable
fun ConfigureZoneSheetContent(
    zone: HealthZoneUiModel,
    onSave: (LocationLabel, HealthProfile, Boolean, Long) -> Unit,
    onCancel: () -> Unit
) {
    var selectedLabel by remember { mutableStateOf(zone.settings?.label ?: LocationLabel.HOME) }
    var selectedProfile by remember { mutableStateOf(zone.settings?.healthProfile ?: HealthProfile.GENERAL) }
    var notificationsEnabled by remember { mutableStateOf(zone.settings?.isNotificationEnabled ?: true) }

    var intervalSliderPos by remember { mutableFloatStateOf(zone.settings?.minNotificationIntervalMinutes?.toFloat() ?: 60f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 32.dp), // Safe area
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Configure Zone: ${zone.location.name}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider()

        Column {
            Text("Label", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(LocationLabel.entries) { label ->
                    SelectableChip(
                        text = label.display,
                        icon = getIconForLabel(label),
                        isSelected = selectedLabel == label,
                        onClick = { selectedLabel = label }
                    )
                }
            }
        }

        Column {
            Text("Health Profile", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
            Text(
                "Alert thresholds adjust based on sensitivity.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(HealthProfile.entries) { profile ->
                    SelectableProfileCard(
                        profile = profile,
                        isSelected = selectedProfile == profile,
                        onClick = { selectedProfile = profile }
                    )
                }
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Alert Frequency", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            }

            if (notificationsEnabled) {
                val hours = (intervalSliderPos / 60).toInt()
                val minutes = (intervalSliderPos % 60).toInt()
                val timeString = if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"

                Text(
                    text = "Don't disturb me more than once every: $timeString",
                    style = MaterialTheme.typography.bodySmall
                )

                Slider(
                    value = intervalSliderPos,
                    onValueChange = { intervalSliderPos = it },
                    valueRange = 15f..360f, // 15 mins to 6 hours
                    steps = 22 // Roughly 15 min increments
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) { Text("Cancel") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    onSave(selectedLabel, selectedProfile, notificationsEnabled, intervalSliderPos.toLong())
                }
            ) {
                Text("Save Zone")
            }
        }
    }
}

@Composable
fun SelectableChip(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.labelLarge, color = contentColor)
    }
}

@Composable
fun SelectableProfileCard(
    profile: HealthProfile,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = getIconForProfile(profile),
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = profile.display,
            style = MaterialTheme.typography.labelMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2,
            minLines = 2
        )
        if (isSelected) {
            Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

fun getIconForLabel(label: LocationLabel): ImageVector {
    return when (label) {
        LocationLabel.HOME -> Icons.Default.Home
        LocationLabel.OFFICE -> Icons.Default.Business
        LocationLabel.SCHOOL -> Icons.Default.ChildCare
        LocationLabel.OUTDOORS -> Icons.Default.Park
        LocationLabel.OTHER -> Icons.Default.Person
    }
}

fun getIconForProfile(profile: HealthProfile): ImageVector {
    return when (profile) {
        HealthProfile.GENERAL -> Icons.Default.Person
        HealthProfile.ASTHMA -> Icons.Default.MedicalServices
        HealthProfile.COPD -> Icons.Default.MedicalServices
        HealthProfile.ELDERLY -> Icons.Default.Elderly
        HealthProfile.CHILD -> Icons.Default.ChildCare
        HealthProfile.PREGNANCY -> Icons.Default.PregnantWoman
        HealthProfile.ATHLETE -> Icons.AutoMirrored.Filled.DirectionsRun
    }
}