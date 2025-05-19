package com.example.jsonproductsmvvm.favproducts.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jsonproductsmvvm.databinding.ActivityFavProductsBinding
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
    private lateinit var binding: ActivityFavProductsBinding
    private val adapter = FavProductsAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorites.adapter = adapter
        val vmFactory = FavProductsViewModelFactory(ProductsRepositoryImpl.getInstance(ProductsRemoteDataSourceImpl(RetrofitHelper.productService), ProductsLocalDataSourceImpl(AppDatabase.getInstance(this).productDao())))
        viewModel = ViewModelProvider(this, vmFactory).get(FavProductsViewModel::class.java)

        viewModel.favorites.observe(this) { favorites ->
            adapter.updateFavorites(favorites)
            Log.d("FavProductsActivity", "Updated UI with ${favorites.size} favorite products")
        }

        viewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.loadFavorites()
    }

    override fun onFavoriteClick(product: Product) {
        viewModel.deleteFavorite(product)
        Log.d("FavProductsActivity", "Attempted to delete ${product.title} from favorites")
    }
}