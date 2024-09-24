package com.example.test2

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpandableItemList(modifier: Modifier) {
    val items = listOf(
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 10"
    )
    val bottomHeight = remember {
        mutableStateOf(1.dp)
    }
    val density = LocalDensity.current

    Box(modifier = modifier.fillMaxSize())
    {
        LazyColumn(
            modifier = modifier.padding(bottom = bottomHeight.value)
        ) {
            item {
                var item = Item(
                    title = "This is Title",
                    banner = "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
                    price = 79.12345,
                    units = 100,
                    closeDate = "2024/10/10",
                    listingDate = "2024/10/08"
                )
                LazyItem(data = item)
            }

            stickyHeader {
                Text(
                    text = "Sticky Header",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(16.dp)
                        .zIndex(1f),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            items(items.size) { index ->
                var isExpanded by remember { mutableStateOf(false) }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        isExpanded = !isExpanded
                    }
                    .background(Color.LightGray)
                    .padding(16.dp)
                    .animateContentSize()
                ) {

                    // Tiêu đề của Item
                    Text(text = items[index], style = MaterialTheme.typography.headlineSmall)
                    // Nội dung chi tiết
                    if (isExpanded) {
                        Text(
                            text = "This is the detailed content for ${items[index]}. You can place more details here.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.LightGray)
                .padding(16.dp)
                .onGloballyPositioned {codinates->
                    bottomHeight.value = with(density){codinates.size.height.toDp()}
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Button 1")
            Text(text = "Button 2")
        }
    }
}

