package com.example.test2

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableItemList(modifier: Modifier) {
    val items = listOf(
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4"
    )
    LazyColumn(
        modifier = modifier
    ) {
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
}

