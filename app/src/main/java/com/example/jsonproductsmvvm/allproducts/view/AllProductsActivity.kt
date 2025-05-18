package com.example.jsonproductsmvvm.allproducts.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonproductsmvvm.R
import com.example.jsonproductsmvvm.allproducts.viewmodel.AllProductsViewModel
import com.example.jsonproductsmvvm.allproducts.viewmodel.AllProductsViewModelFactory
import com.example.jsonproductsmvvm.db.AppDatabase
import com.example.jsonproductsmvvm.db.ProductsLocalDataSourceImpl
import com.example.jsonproductsmvvm.model.Product
import com.example.jsonproductsmvvm.model.ProductsRepositoryImpl
import com.example.jsonproductsmvvm.network.ProductsRemoteDataSourceImpl
import com.example.jsonproductsmvvm.network.RetrofitHelper

class AllProductsActivity : AppCompatActivity(), OnProductClickListener {

    private lateinit var viewModel: AllProductsViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter = AllProductsAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_products)

        recyclerView = findViewById(R.id.recycler_view_products)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val vmFactory = AllProductsViewModelFactory(ProductsRepositoryImpl.getInstance(ProductsRemoteDataSourceImpl(RetrofitHelper.productService), ProductsLocalDataSourceImpl(AppDatabase.getInstance(this).productDao())))
        viewModel = ViewModelProvider(this, vmFactory).get(AllProductsViewModel::class.java)

        viewModel.products.observe(this) { products ->
            adapter.updateProducts(products)
            Log.d("AllProductsActivity", "Updated UI with ${products.size} products")
        }

        viewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchProducts()
    }

    override fun onProductClick(product: Product) {
        viewModel.addToFavorites(product)
        Log.d("AllProductsActivity", "Attempted to add ${product.title} to favorites")
    }
}