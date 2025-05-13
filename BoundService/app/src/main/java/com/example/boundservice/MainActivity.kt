package com.example.datetimeapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.boundservice.R

class MainActivity : AppCompatActivity() {
    //7
    private lateinit var myService: BoundService
    private var isBound: Boolean = false

    //8&9
    private var myConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: BoundService.MyLocalBinder = service as BoundService.MyLocalBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getTimeButton = findViewById<Button>(R.id.getTimeButton)
        getTimeButton.setOnClickListener { view ->
            showTime(view)
        }

        val intent = Intent(this, BoundService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    fun showTime(view: View) {
        val currentTime: String = myService.getCurrentTime()
        Log.i("TAG", "showTime: $currentTime")
        findViewById<TextView>(R.id.dateTimeTextView).text = currentTime
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(myConnection)
            isBound = false
        }
    }
}