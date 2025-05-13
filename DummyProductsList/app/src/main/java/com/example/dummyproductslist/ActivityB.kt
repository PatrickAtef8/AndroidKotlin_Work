package com.example.dummyproductslist


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        val productId = intent.getIntExtra("product_id", 1)
        val bundle = Bundle()
        bundle.putInt("product_id", productId)
        val fragmentB = FragmentB()
        fragmentB.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_b_container, fragmentB)
            .commit()
    }
}