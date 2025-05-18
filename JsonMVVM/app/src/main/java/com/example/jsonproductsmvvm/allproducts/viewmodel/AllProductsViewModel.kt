package com.example.jsonproductsmvvm.allproducts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonproductsmvvm.model.Product
import com.example.jsonproductsmvvm.model.ProductsRepository
import kotlinx.coroutines.launch

class AllProductsViewModel(private val repository: ProductsRepository) : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun fetchProducts() {
        viewModelScope.launch {
            val products = repository.getProducts(true)
            _products.value = products
        }
    }

    fun addToFavorites(product: Product) {
        viewModelScope.launch {
            repository.addFavorite(product)
        }
    }
}