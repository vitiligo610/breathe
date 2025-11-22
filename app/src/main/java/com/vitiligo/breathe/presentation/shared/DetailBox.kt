package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailBox(
    label: String,
    modifier: Modifier = Modifier,
    headerContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val cornerRadius = 12.dp
    val outlineWidth = 1.dp
    val boxShape = RoundedCornerShape(cornerRadius)

    Column(
        modifier = modifier
            .border(
                width = outlineWidth,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = boxShape
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
            )
            headerContent()
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp)
                .wrapContentSize()
        ) {
            content()
        }
    }
}