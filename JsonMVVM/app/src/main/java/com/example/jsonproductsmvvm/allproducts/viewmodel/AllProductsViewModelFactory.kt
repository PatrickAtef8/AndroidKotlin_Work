package com.example.jsonproductsmvvm.allproducts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jsonproductsmvvm.model.ProductsRepository

class AllProductsViewModelFactory(private val _irepo: ProductsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AllProductsViewModel::class.java)) {
            AllProductsViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}