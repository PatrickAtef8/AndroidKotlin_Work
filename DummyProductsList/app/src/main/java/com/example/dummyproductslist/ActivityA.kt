package com.example.dummyproductslist

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class ActivityA : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_a_container, FragmentA())
                .commit()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentBContainer = findViewById<View>(R.id.fragment_b_container)
            if (fragmentBContainer != null) {
                fragmentBContainer.visibility = View.VISIBLE
                val fragmentB = FragmentB()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_b_container, fragmentB, "FragmentB")
                    .commit()
            } else {
                Log.e("ActivityA", "fragment_b_container not found in layout")
            }
        }
    }

    fun showProductDetails(productId: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentBContainer = findViewById<View>(R.id.fragment_b_container)
            if (fragmentBContainer != null) {
                val bundle = Bundle()
                bundle.putInt("product_id", productId)
                val fragmentB = FragmentB()
                fragmentB.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_b_container, fragmentB, "FragmentB")
                    .addToBackStack(null)
                    .commit()
            } else {
                Log.e("ActivityA", "fragment_b_container not found, launching ActivityB")
                val intent = Intent(this, ActivityB::class.java)
                intent.putExtra("product_id", productId)
                startActivity(intent)
            }
        } else {
            val intent = Intent(this, ActivityB::class.java)
            intent.putExtra("product_id", productId)
            startActivity(intent)
        }
    }
}