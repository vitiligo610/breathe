package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.vitiligo.breathe.domain.model.HomeTab

@Composable
fun HomeContent(
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(HomeTab.GLOBAL.ordinal) }
    val tabs = HomeTab.entries.toTypedArray()

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(tab.label) }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (tabs[selectedTabIndex]) {
                HomeTab.MY_SENSORS -> MySensorsTabContent()
                HomeTab.GLOBAL -> GlobalTabContent()
            }
        }
    }
}
