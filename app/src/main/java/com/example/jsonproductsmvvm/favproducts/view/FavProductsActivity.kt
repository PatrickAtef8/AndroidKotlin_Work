package com.example.jsonproductsmvvm.favproducts.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonproductsmvvm.R
import com.example.jsonproductsmvvm.favproducts.viewmodel.FavProductsViewModel
import com.example.jsonproductsmvvm.favproducts.viewmodel.FavProductsViewModelFactory
import com.example.jsonproductsmvvm.db.AppDatabase
import com.example.jsonproductsmvvm.db.ProductsLocalDataSourceImpl
import com.example.jsonproductsmvvm.model.Product
import com.example.jsonproductsmvvm.model.ProductsRepositoryImpl
import com.example.jsonproductsmvvm.network.ProductsRemoteDataSourceImpl
import com.example.jsonproductsmvvm.network.RetrofitHelper

class FavProductsActivity : AppCompatActivity(), OnFavoriteClickListener {

    private lateinit var viewModel: FavProductsViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter = FavProductsAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_products)

        recyclerView = findViewById(R.id.recycler_view_favorites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val vmFactory = FavProductsViewModelFactory(ProductsRepositoryImpl.getInstance(ProductsRemoteDataSourceImpl(RetrofitHelper.productService), ProductsLocalDataSourceImpl(AppDatabase.getInstance(this).productDao())))
        viewModel = ViewModelProvider(this, vmFactory).get(FavProductsViewModel::class.java)

        viewModel.favorites.observe(this) { favorites ->
            adapter.updateFavorites(favorites)
            Log.d("FavProductsActivity", "Updated UI with ${favorites.size} favorite products")
        }
        viewModel.loadFavorites()
    }

    override fun onFavoriteClick(product: Product) {
        viewModel.deleteFavorite(product)
        Log.d("FavProductsActivity", "Deleted ${product.title} from favorites")
    }
}