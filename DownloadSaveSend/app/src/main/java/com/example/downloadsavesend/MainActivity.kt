package com.example.downloadsavesend

import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private lateinit var receiver: ImageReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlInput = findViewById<EditText>(R.id.urlInput)
        val downloadButton = findViewById<Button>(R.id.downloadButton)
        receiver = ImageReceiver { uri ->
            imageUri = uri
            displayImage(imageUri!!)
            val intent = Intent(this, ImageViewActivity::class.java).apply {
                putExtra("image_uri", uri.toString())
            }
            startActivity(intent)
        }
        val filter = IntentFilter("IMAGE_DOWNLOADED")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

        downloadButton.setOnClickListener {
            val url = urlInput.text.toString().trim()
            if (url.isNotEmpty()) {
                val intent = Intent(this, ImageDownloadService::class.java).apply {
                    putExtra("url", url)
                }
                startService(intent)
            }
        }

        intent.getStringExtra("image_uri")?.let { uriString ->
            imageUri = Uri.parse(uriString)
            displayImage(imageUri!!)
        }
    }

    private fun displayImage(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap?.let { imageView.setImageBitmap(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
}