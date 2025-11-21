package com.vitiligo.breathe.ui.location_details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.presentation.location_details.LocationDetailsContent
import com.vitiligo.breathe.presentation.location_details.LocationDetailsTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            LocationDetailsTopBar(
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) {
        LocationDetailsContent(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}