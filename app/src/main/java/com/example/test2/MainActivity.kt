package com.example.test2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.test2.ui.theme.Test2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    ExpandableItemList(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }
    var showReadMore by remember { mutableStateOf(true) }
    var height by remember {
        mutableStateOf(0)
    }

    val context = LocalContext.current
    val webView = remember {
        WebView(context)
    }
    LazyColumn {
        item {
            ExoPlayerView()
        }
        item {
                val items = listOf(
                    TimelineItem("No current plan", "Food App", isActive = false),
                    TimelineItem("Today", "Battery App", isActive = true),
                    TimelineItem("Tomorrow", "Food App", isActive = false),
                    TimelineItem("Next week", "Upcoming task", isActive = false)
                )
                VerticalTimeline(items)
        }
        item(key = "webviewItem") {
            val html = """
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
        <p>This is a paragraph.</p>
    """.trimIndent()
            val density = LocalDensity.current
            Box {
                MarkDown(
                    htmlContent = html,
                    modifier = (if (!isExpanded) Modifier.height(144.dp) else Modifier)
                        .fillMaxWidth(),
                    returnContentHeight = { contentHeight ->
                        showReadMore = contentHeight!!.dp > 144.dp
                    },
                    webView = webView
                )
                if (showReadMore && !isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .graphicsLayer {
                                // Apply blur effect
                                renderEffect = BlurEffect(radiusX = 10f, radiusY = 10f)
                            }
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.9f),  // Blur stronger here
                                        Color.Transparent  // Blur fades out here
                                    )
                                )
                            )
                            .align(Alignment.BottomCenter)
                    )
                }
            }
            if (showReadMore) {
                Box {
                    if (!isExpanded) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(16.dp)
//                                .background(
//                                    Brush.verticalGradient(
//                                        colors = listOf(Color.Transparent, Color.White)
//                                    )
//                                )
//                                .align(Alignment.TopCenter)
//                                .padding(bottom = with(density){height.toDp()})
//
//                        )
                    }
                    Text(
                        text = if (isExpanded) "Read Less" else "Read More",
                        modifier = Modifier
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        color = Color.Blue,
                    )
                }
            }
        }

        item {
            CircularProgressIndicator()
        }
        items(100) { index ->
            Text(text = "Item: $index")
        }
    }
    webView.addOnLayoutChangeListener { _: View, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int ->
        showReadMore = webView.contentHeight.dp > 114.dp
    }
}

@Composable
fun ColoredTextExample(modifier: Modifier = Modifier) {
    val text = AnnotatedString.Builder().apply {
        append("This is a ")
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("colored")
        }
        append(" text.")
    }.toAnnotatedString()
    var list = (1..20).toList()
    list.filter { return@filter it > 10 }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
fun VideoBanner(videoUrl: String) {
    val context = LocalContext.current


    // Sử dụng AndroidView để nhúng VideoView
    AndroidView(
        factory = { ctx ->
            val videoView = VideoView(ctx)
            val mediaController = MediaController(ctx)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(videoUrl))
            videoView.start()
            videoView
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(400.dp)
    )


}


@Composable
fun ExoPlayerView() {
    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    val uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    // Create a MediaSource

    val mediaSource = remember(uri) {
        MediaItem.fromUri(uri)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false
            }
        },
        modifier = Modifier
            .padding(30.dp)
            .padding(top = 50.dp)
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Test2Theme {
        Greeting("Android")
    }
}