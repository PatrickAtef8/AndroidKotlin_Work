package com.example.geosms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.locationdetection.R
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvLon: TextView
    private lateinit var tvLat: TextView
    private lateinit var tvAddress: TextView
    private val geo by lazy { Geocoder(this, Locale.getDefault()) }

    private var latitude: Double? = null
    private var longitude: Double? = null
    private var addressText: String? = null

    companion object {
        private const val MY_LOCATION_PERMISSION_ID = 5005
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLon = findViewById(R.id.tvLongitude)
        tvLat = findViewById(R.id.tvLatitude)
        tvAddress = findViewById(R.id.tvAddress)

        findViewById<Button>(R.id.btnOpenSMS).setOnClickListener { openSms() }
        findViewById<Button>(R.id.btnOpenMap).setOnClickListener { openMapFragment() }

        requestPermissionAndFetchLocation()
    }

    private fun requestPermissionAndFetchLocation() {
        if (checkPermissions()) {
            fetchLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_LOCATION_PERMISSION_ID
            )
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
                tvAddress.text = "Location permission denied"
            }
        }
    }

    private fun fetchLocation() {
        val client = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            tvAddress.text = "Missing location permissions"
            return
        }

        client.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                longitude = loc.longitude
                latitude = loc.latitude
                tvLon.text = longitude.toString()
                tvLat.text = latitude.toString()

                // Perform geocoding off the UI thread
                Thread {
                    addressText = try {
                        val list = geo.getFromLocation(latitude!!, longitude!!, 1)
                        list?.getOrNull(0)?.getAddressLine(0) ?: "Unknown address"
                    } catch (e: IOException) {
                        "Unable to get address"
                    }
                    // Post result back to UI
                    runOnUiThread {
                        tvAddress.text = addressText
                    }
                }.start()

            } else {
                tvAddress.text = "Could not get location"
            }
        }.addOnFailureListener {
            tvAddress.text = "Error fetching location"
        }
    }

    private fun openSms() {
        val phone = "01287712761"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phone")
            putExtra("sms_body", addressText ?: "No location available")
        }
        startActivity(intent)
    }

    private fun openMapFragment() {
        if (latitude != null && longitude != null) {
            val bundle = Bundle().apply {
                putDouble("lat", latitude!!)
                putDouble("lon", longitude!!)
            }
            val fragment = MapFragment().apply { arguments = bundle }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            tvAddress.text = "Location not available yet"
        }
    }
}
