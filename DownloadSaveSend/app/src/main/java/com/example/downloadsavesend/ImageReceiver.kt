package com.example.downloadsavesend

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri

class ImageReceiver(private val onUriReceived: (Uri) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val uriString = intent?.getStringExtra("uri")
        if (uriString != null) {
            val uri = Uri.parse(uriString)
            onUriReceived(uri)
        }
    }
}