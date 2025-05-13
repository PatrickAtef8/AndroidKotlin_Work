package com.example.downloadsavesend

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.net.URL

class ImageDownloadService : IntentService("ImageDownloadService") {

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("url")
        if (url != null) {
            try {
                val connection = URL(url).openConnection()
                val bitmap = BitmapFactory.decodeStream(connection.getInputStream())
                if (bitmap != null) {
                    val uri = saveImage(bitmap)
                    if (uri != android.net.Uri.EMPTY) {
                        val broadcastIntent = Intent("IMAGE_DOWNLOADED").apply {
                            putExtra("uri", uri.toString())
                        }
                        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
                    }
                }
            } catch (_: Exception) {}
        }
    }

    private fun saveImage(bitmap: Bitmap): android.net.Uri {
        val contentResolver = contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "downloaded_image_${System.currentTimeMillis()}.jpg")
            put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DownloadedImages")
        }
        val imageUri = contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return try {
            imageUri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                it
            } ?: android.net.Uri.EMPTY
        } catch (_: Exception) {
            android.net.Uri.EMPTY
        }
    }
}