package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class SegmentOption(
    val key: String,
    val label: String
)

@Composable
fun SegmentedControl(
    options: List<SegmentOption>,
    selectedKey: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (options.size != 2) {
        return
    }

    val shape = CircleShape
    val borderWidth = 1.dp
    val primaryColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(borderWidth, primaryColor, shape)
            .height(48.dp)
            .width(IntrinsicSize.Min)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option.key == selectedKey

            Segment(
                option = option,
                isSelected = isSelected,
                onClick = { onOptionSelected(option.key) },
                showDivider = index == 0,
                dividerColor = primaryColor,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun Segment(
    option: SegmentOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    showDivider: Boolean,
    dividerColor: Color,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) dividerColor else MaterialTheme.colorScheme.surfaceContainer
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = option.label,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (showDivider) {
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(dividerColor)
        )
    }
}