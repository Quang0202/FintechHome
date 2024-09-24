package com.example.test2

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Composable
fun PdfViewerScreen(
    modifier: Modifier = Modifier,
    pdfUrl: String
) {
    val context = LocalContext.current
    val pdfBitmapConverter = remember {
        PdfBitmapConverter(context)
    }
    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var renderedPages by remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }
    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            renderedPages = pdfBitmapConverter.pdfToBitmaps(uri)
        }
    }
    LaunchedEffect(pdfUrl) {
        val tempFile = File(context.cacheDir, "temp.pdf")
        downloadPdfFromUrl(pdfUrl, tempFile.absolutePath) { file ->
            pdfFile = file
            pdfUri = Uri.fromFile(file)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.d("ABC123","dispose")
            pdfFile?.let {
                if (it.exists()) {
                    it.delete()
                }
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(renderedPages) { index, page ->
                PdfPage(
                    page = page
                )
            }
        }
    }
}

@Composable
fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
            .drawWithContent {
                drawContent()

            }
    )
}

fun downloadPdfFromUrl(url: String, filePath: String, onComplete: (File) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.body?.let { responseBody ->
                val file = File(filePath)
                val fos = FileOutputStream(file)
                fos.use { outputStream ->
                    outputStream.write(responseBody.bytes())
                }
                onComplete(file)
            }
        }
    })
}