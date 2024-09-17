package com.example.test2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TimelineItem(
    val title: String,
    val description: String,
    val isActive: Boolean
)

@Composable
fun VerticalTimeline(items: List<TimelineItem>) {
   Column(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
    ) {
       for(index in items.indices) {
           val item = items[index]
           val isLeftAligned = index % 2 == 0
           TimelineItemView(item, isLeftAligned)
       }
    }
}
@Composable
fun TimelineItemView(item: TimelineItem, isLeftAligned: Boolean) {
    val circleColor = if (item.isActive) Color.Green else Color.Gray
    val circleSize = 12.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 10.dp)
    ) {
        if (isLeftAligned) {
            TimelineContent(item)
            Spacer(modifier = Modifier.width(16.dp))
        }else{
            Spacer(modifier = Modifier.width(166.dp) )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle
            Canvas(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
            ) {
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            // Dotted line
            Spacer(modifier = Modifier.height(8.dp))
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp),
            ) {
                drawLine(
                    color = Color.Gray,
                    start = center.copy(y = 0f),
                    end = center.copy(y = size.height),
                    strokeWidth = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                    cap = StrokeCap.Round
                )
            }
        }

        if (!isLeftAligned) {
            Spacer(modifier = Modifier.width(16.dp))
            TimelineContent(item)
        }
    }
}

@Composable
fun TimelineContent(item: TimelineItem) {
    Column(modifier = Modifier.width(150.dp).padding(start = 20.dp)) {
        Text(text = item.title, style = TextStyle(fontSize = 18.sp, color = Color.Black))
        Text(text = item.description, style = TextStyle(fontSize = 14.sp, color = Color.Gray))
    }
}