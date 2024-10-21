package com.example.test2

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun DownloadImage() {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    Button(onClick = {
        if (permissionState.status.isGranted) {
            // Nếu permission đã được cấp, thực hiện lưu hình ảnh
            saveImage(context, bitmap)
        } else {
            // Nếu chưa được cấp, yêu cầu quyền truy cập
            permissionState.launchPermissionRequest()
        }
    }) {
        Text("Download Image")
    }

    // Khi trạng thái của quyền thay đổi, ta xử lý tương ứng
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            // Nếu người dùng cấp quyền, tiến hành lưu ảnh ngay lập tức
            saveImage(context, bitmap)
        } else if (permissionState.status.shouldShowRationale) {
            // Hiển thị hộp thoại giải thích nếu cần
            Toast.makeText(
                context,
                "This app needs storage access to save images.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

fun saveImage(context: Context, bitmap: Bitmap) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveBitmapToGallery(context, bitmap, "my_image")
    } else {
        saveBitmapLegacy(context, bitmap, "my_image")
    }
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, fileName: String) {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        contentResolver.openOutputStream(uri).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
    }
}

fun saveBitmapLegacy(context: Context, bitmap: Bitmap, fileName: String) {
    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file = File(directory, "$fileName.jpg")

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }

    // Thông báo cho MediaStore về file mới
    MediaScannerConnection.scanFile(
        context,
        arrayOf(file.toString()),
        null,
        null
    )
}
