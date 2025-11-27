package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.domain.model.HomeTab
import com.vitiligo.breathe.ui.home.HomeViewModel

@Composable
fun HomeContent(
    navigateToLocationDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(HomeTab.GLOBAL.ordinal) }
    val tabs = HomeTab.entries.toTypedArray()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(tab.label) }
                )
            }
        }


        when (tabs[selectedTabIndex]) {
            HomeTab.GLOBAL -> GlobalTabContent(
                navigateToLocationDetails = navigateToLocationDetails,
                locations = state.locations,
                onRefresh = viewModel::refreshData,
                isRefreshing = state.isRefreshing
            )
            HomeTab.MY_SENSORS -> MySensorsTabContent()
        }
    }
}
