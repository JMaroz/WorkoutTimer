package com.marozzi.desing.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marozzi.desing.theme.SPACE_HIGH
import com.marozzi.desing.theme.SPACE_MEDIUM
import timber.log.Timber

@Composable
fun <T> SimpleLazyRow(
    modifier: Modifier,
    items: List<T>,
    key: ((T) -> Any)? = null,
    itemContent: (T) -> String,
    onSelected: (T) -> Unit,
) {
    var selectedItem by remember { mutableStateOf(items.firstOrNull()) }
    LaunchedEffect(key1 = items) {
        selectedItem?.let(onSelected)
    }
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACE_MEDIUM),
        contentPadding = PaddingValues(all = SPACE_HIGH),
        content = {
            items(
                items = items,
                key = key,
                itemContent = {
                    SimpleLazyItem(
                        item = it,
                        selected = it == selectedItem,
                        itemContent = itemContent
                    ) {
                        selectedItem = it
                        onSelected(it)
                    }
                },
            )
        },
    )
}

@Composable
fun <T> SimpleLazyItem(
    item: T,
    selected: Boolean,
    itemContent: (T) -> String,
    onClick: (T) -> Unit,
) {
    var focusBt by remember {
        mutableStateOf(false)
    }

    RoundSurface(
        onClick = { onClick(item) },
        modifier = Modifier
            .size(width = 156.dp, height = 60.dp)
            .onFocusChanged {
                focusBt = it.hasFocus
            }
            .focusable(),
        borderColor = if (selected)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface.copy(alpha = .1f),
        backgroundColor = if (selected)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface.copy(alpha = .8f),
        shadowElevation = if (focusBt) 8.dp else 0.dp
    ) {
        RoundBox(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val txt = itemContent(item)
            TextBody(
                text = txt,
                color = if (selected)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = .8f),
                upperCase = true
            )
        }
    }
}

@Preview(device = Devices.PHONE)
@Composable
private fun RoundBoxTest() {
    MaterialTheme {
        val items = (0..10).map { it.toString() }
        SimpleLazyRow(
            modifier = Modifier,
            items = items,
            itemContent = { it },
            key = { it },
            onSelected = {

            }
        )
    }
}

