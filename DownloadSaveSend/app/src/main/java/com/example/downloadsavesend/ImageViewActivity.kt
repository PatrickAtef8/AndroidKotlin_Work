package com.example.downloadsavesend

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val uriString = intent.getStringExtra("image_uri")
        if (uriString != null) {
            val uri = Uri.parse(uriString)
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap?.let { imageView.setImageBitmap(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            finish()
        }
    }
}
