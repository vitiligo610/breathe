package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.vitiligo.breathe.ui.theme.serifFontFamily

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineLarge
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.ic_breathe_logo),
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.size(style.fontSize.value.dp)
//        )
//        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Breathe",
            color = MaterialTheme.colorScheme.primary, // Uses your custom Blue
            fontFamily = serifFontFamily,
            style = style
        )
    }
}