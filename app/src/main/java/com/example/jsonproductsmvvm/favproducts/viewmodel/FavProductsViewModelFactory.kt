package com.example.jsonproductsmvvm.favproducts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jsonproductsmvvm.model.ProductsRepository

class FavProductsViewModelFactory(private val _irepo: ProductsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavProductsViewModel::class.java)) {
            FavProductsViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}