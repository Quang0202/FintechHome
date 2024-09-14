package com.example.test2

import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MarkDown(htmlContent: String, modifier: Modifier = Modifier, returnContentHeight: ((Int)->Unit)? = null, webView: WebView) {
    Box(modifier = modifier)
    {
        AndroidView(
            factory = {
                webView
            },
            update = { webView ->
                webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                webView.addOnLayoutChangeListener { _: View, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int ->
                    returnContentHeight?.let { it(webView.contentHeight) }
                    Log.d("ABC123", "${webView.contentHeight}")
                }
            }
        )
    }
}
