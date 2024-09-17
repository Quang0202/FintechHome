package com.example.test2

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MarkDown(
    htmlContent: String,
    modifier: Modifier = Modifier,
    returnContentHeight: ((Int?) -> Unit)? = null,
    webView: WebView?= null
) {
//    val webViewState = rememberSaveable  { mutableStateOf<WebView?>(null) }
    val escapedHtmlContent = htmlContent.replace("\"", "\\\"").replace("\n", "\\n")
    val add = "javascript:addHTML(\"$escapedHtmlContent\")"
    Box(modifier = modifier)
    {
        AndroidView(
            factory = {context ->
                (webView ?: WebView(context)).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    this.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            view?.evaluateJavascript(add, null)
                        }

                    }
                    this.addOnLayoutChangeListener { _: View, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int ->
                        returnContentHeight?.let { it(webView?.contentHeight) }
                        Log.d("ABC123", "${webView?.contentHeight}")
                    }
                    settings.javaScriptEnabled = true // Nếu cần sử dụng JavaScript
                    loadUrl("file:///android_asset/input.html")
                }
            },
            modifier = Modifier.background(color = Color.Transparent)
        )
    }
}
