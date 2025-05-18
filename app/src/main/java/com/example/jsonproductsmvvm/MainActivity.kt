package com.example.jsonproductsmvvm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jsonproductsmvvm.allproducts.view.AllProductsActivity
import com.example.jsonproductsmvvm.favproducts.view.FavProductsActivity
import com.example.jsonproductsmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show All Products button
        binding.buttonShowAllProducts.setOnClickListener {
            val intent = Intent(this, AllProductsActivity::class.java)
            startActivity(intent)
        }

        // Show Favorites button
        binding.buttonShowFavorites.setOnClickListener {
            val intent = Intent(this, FavProductsActivity::class.java)
            startActivity(intent)
        }

        // Exit button
        binding.buttonExit.setOnClickListener {
            finishAffinity() // Closes the app completely
        }
    }
}