package com.vitiligo.breathe.ui.location_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.presentation.location_details.LocationDetailsContent
import com.vitiligo.breathe.presentation.location_details.LocationDetailsTopBar
import com.vitiligo.breathe.presentation.location_details.StickyLocationHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val showStickyDetails by remember {
        derivedStateOf {
            val thresholdPx = with(density) { 500.dp.toPx() }
            lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > thresholdPx
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                LocationDetailsTopBar(
                    navigateBack = navigateBack,
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            LocationDetailsContent(
                lazyListState = lazyListState,
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (showStickyDetails) {
            StickyLocationHeader(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 50.dp)
            )
        }
    }
}
