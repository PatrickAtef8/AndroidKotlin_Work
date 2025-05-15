package com.example.jsonproductsviewbinding

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jsonproductsviewbinding.databinding.ActivityABinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityA : AppCompatActivity() {

    private var products: List<Products> = emptyList()
    private lateinit var database: AppDatabase
    private lateinit var binding: ActivityABinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityABinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_a_container, FragmentA())
                .commit()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentBContainer = binding.fragmentBContainer
            if (fragmentBContainer != null) {
                fragmentBContainer.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_b_container, FragmentB(), "FragmentB")
                    .commit()
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtils.isNetworkConnected(this@ActivityA)) {
                    val response = RetrofitClient.apiService.getProducts()
                    if (response.isSuccessful) {
                        products = response.body()?.products ?: emptyList()
                        database.productDao().clearAll()
                        database.productDao().insertAll(products)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ActivityA, "Products loaded from network", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        products = database.productDao().getAllProducts()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ActivityA, "Network error, loaded from room", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    products = database.productDao().getAllProducts()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ActivityA, "Offline, loaded from room", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                products = database.productDao().getAllProducts()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ActivityA, "Error: ${e.message}, loaded from room", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showProductDetails(productId: Int) {
        val product = products.find { it.id == productId }
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentBContainer = binding.fragmentBContainer
            if (fragmentBContainer != null && product != null) {
                val bundle = Bundle()
                bundle.putInt("product_id", product.id)
                bundle.putString("product_title", product.title)
                bundle.putString("product_description", product.description)

                val fragmentB = FragmentB()
                fragmentB.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_b_container, fragmentB, "FragmentB")
                    .addToBackStack(null)
                    .commit()
            } else {
                startActivityB(productId)
            }
        } else {
            startActivityB(productId)
        }
    }

    private fun startActivityB(productId: Int) {
        val intent = Intent(this, ActivityB::class.java)
        intent.putExtra("product_id", productId)
        startActivity(intent)
    }
}