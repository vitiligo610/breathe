package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.util.getAqiCategoryColor
import com.vitiligo.breathe.domain.util.getOnAqiCategoryColor

@Composable
fun AqiValueBadge(
    value: Int,
    category: AqiCategory,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(getAqiCategoryColor(category))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = getOnAqiCategoryColor(category),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}