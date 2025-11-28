package com.vitiligo.breathe.presentation.location_details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.vitiligo.breathe.presentation.shared.AppLogo

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocationDetailsTopBar(
    navigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    actionsDisabled: Boolean,
    hideDeleteOption: Boolean,
    onDeleteLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { navigateBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = { AppLogo() },
        actions = {
            IconButton(
                onClick = { showBottomSheet = true },
                enabled = !actionsDisabled
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More actions"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )

    if (showBottomSheet) {
        ActionBottomSheet(
            sheetState = sheetState,
            coroutineScope = coroutineScope,
            onDismissRequest = { showBottomSheet = false },
            onDelete = { showDeleteDialog = true },
            hideDeleteOption = hideDeleteOption,
        )
    }

    if (showDeleteDialog) {
        DeleteAlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            onDelete = onDeleteLocation
        )
    }
}