package com.example.test2

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PdfView(pdfUrl: String) {
    val context = LocalContext.current
    val pdfJsHtml = """
        <html>file:///android_asset/pdfjs/build/pdf.js
        <head>
            <script src="file:///android_asset/pdfjs/build/pdf.js"></script>
        </head>
        <body>
            <canvas id="the-canvas"></canvas>
            <script>
                var url = '$pdfUrl';
                var pdfjsLib = window['pdfjs-dist/build/pdf'];
                pdfjsLib.GlobalWorkerOptions.workerSrc = 'file:///android_asset/pdfjs/build/pdf.worker.js';
                var loadingTask = pdfjsLib.getDocument(url);
                loadingTask.promise.then(function(pdf) {
                    pdf.getPage(1).then(function(page) {
                        var scale = 1.5;
                        var viewport = page.getViewport({scale: scale});
                        var canvas = document.getElementById('the-canvas');
                        var context = canvas.getContext('2d');
                        canvas.height = viewport.height;
                        canvas.width = viewport.width;
                        var renderContext = {
                            canvasContext: context,
                            viewport: viewport
                        };
                        page.render(renderContext);
                    });
                });
            </script>
        </body>
        </html>
    """.trimIndent()

    AndroidView(factory = { WebView(context) }, update = { webView ->
        webView.settings.javaScriptEnabled = true
        webView.loadDataWithBaseURL(null, pdfJsHtml, "text/html", "UTF-8", null)
    })
}