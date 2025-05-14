package com.example.jsonproductscoroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityB : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        database = AppDatabase.getDatabase(this)
        val productId = intent.getIntExtra("product_id", 1)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val product = if (NetworkUtils.isNetworkConnected(this@ActivityB)) {
                    val response = RetrofitClient.apiService.getProducts()
                    if (response.isSuccessful) {
                        response.body()?.products?.find { it.id == productId }?.also {
                            withContext(Dispatchers.Main) { Toast.makeText(this@ActivityB, "Product loaded from network", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        database.productDao().getAllProducts().find { it.id == productId }?.also {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@ActivityB, "Network error, loaded from room", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    database.productDao().getAllProducts().find { it.id == productId }?.also {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ActivityB, "Offline, loaded from room", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                product?.let {
                    withContext(Dispatchers.Main) {
                        val bundle = Bundle()
                        bundle.putInt("product_id", it.id)
                        bundle.putString("product_title", it.title)
                        bundle.putString("product_description", it.description)

                        val fragmentB = FragmentB()
                        fragmentB.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_b_container, fragmentB)
                            .commit()
                    }
                } ?: withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ActivityB,
                        "Product not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                val product = database.productDao().getAllProducts().find { it.id == productId }
                if (product != null) {
                    withContext(Dispatchers.Main) {
                        val bundle = Bundle()
                        bundle.putInt("product_id", product.id)
                        bundle.putString("product_title", product.title)
                        bundle.putString("product_description", product.description)

                        val fragmentB = FragmentB()
                        fragmentB.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_b_container, fragmentB)
                            .commit()

                        Toast.makeText(
                            this@ActivityB,
                            "Error: ${e.message}, loaded from room",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ActivityB,
                            "Error: ${e.message}, product not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}