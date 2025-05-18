package com.example.jsonproductsmvvm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jsonproductsmvvm.db.AppDatabase
import com.example.jsonproductsmvvm.db.ProductsLocalDataSource
import com.example.jsonproductsmvvm.db.ProductsLocalDataSourceImpl
import com.example.jsonproductsmvvm.model.Product
import com.example.jsonproductsmvvm.network.ProductService
import com.example.jsonproductsmvvm.network.ProductsRemoteDataSource
import com.example.jsonproductsmvvm.network.ProductsRemoteDataSourceImpl
import com.example.jsonproductsmvvm.network.RetrofitHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var localDataSource: ProductsLocalDataSource
    private lateinit var remoteDataSource: ProductsRemoteDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(this)
        localDataSource = ProductsLocalDataSourceImpl(db.productDao())
        val productService: ProductService = RetrofitHelper.productService
        remoteDataSource = ProductsRemoteDataSourceImpl(productService)

        lifecycleScope.launch {
            try {
                val response = remoteDataSource.fetchProducts()
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    products.forEach { product ->
                        localDataSource.insertProduct(product)
                    }
                    val allProducts = localDataSource.getAllProducts()
                    if (allProducts.isNotEmpty()) {
                        localDataSource.deleteProduct(allProducts[0])
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}