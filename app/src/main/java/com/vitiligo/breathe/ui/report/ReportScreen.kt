package com.vitiligo.breathe.ui.report

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitiligo.breathe.presentation.report.ReportContent
import com.vitiligo.breathe.presentation.report.ReportTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navigateBack: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.submitResult) {
        when (uiState.submitResult) {
            true -> {
                Toast.makeText(context, "Report Submitted Successfully!", Toast.LENGTH_LONG).show()
                navigateBack()
                viewModel.resetState()
            }
            false -> {
                Toast.makeText(context, "Failed to submit report.", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            null -> {  }
        }
    }

    Scaffold(
        topBar = { ReportTopBar(navigateBack = navigateBack) }
    ) { padding ->
        ReportContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            onTypeChange = viewModel::onTypeChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onSubmitReport = viewModel::submitReport
        )
    }
}