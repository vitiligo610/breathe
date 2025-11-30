package com.vitiligo.breathe.presentation.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.domain.model.LocationSearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<LocationSearchResult>,
    onResultClick: (LocationSearchResult) -> Unit,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { isOpen ->
                        expanded = isOpen
                        onToggle(isOpen)
                    },
                    placeholder = { Text("Search city, region...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (expanded && query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                )
            },
            expanded = expanded,
            onExpandedChange = { isOpen ->
                expanded = isOpen
                onToggle(isOpen)
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { traversalIndex = 0f }
        ) {
            LazyColumn {
                items(results) { result ->
                    ListItem(
                        headlineContent = { Text(result.name) },
                        supportingContent = { Text(result.region) },
                        leadingContent = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                expanded = false
                                onToggle(false)
                                onResultClick(result)
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}