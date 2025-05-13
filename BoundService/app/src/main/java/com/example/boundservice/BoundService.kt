package com.example.datetimeapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//1
class BoundService : Service() {
    //4 obj from binder class
    private val myBinder: IBinder = MyLocalBinder()


    override fun onBind(intent: Intent): IBinder {
        //5 ret binder obj
        return myBinder
    }
    //3 to be called by the client
    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US)
        return dateFormat.format(Date())
    }

    //2
    inner class MyLocalBinder : Binder() {
        //2a
        fun getService(): BoundService {
            return this@BoundService
        }
    }
}
