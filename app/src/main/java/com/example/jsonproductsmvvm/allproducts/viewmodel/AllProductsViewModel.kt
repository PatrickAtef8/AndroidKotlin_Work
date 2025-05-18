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

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun fetchProducts() {
        viewModelScope.launch {
            val products = repository.getProducts(true)
            _products.value = products
        }
    }

    fun addToFavorites(product: Product) {
        viewModelScope.launch {
            val existingFavorites = repository.getFavorites()
            if (existingFavorites.none { it.id == product.id }) {
                repository.addFavorite(product)
                _message.value = "${product.title} added to favorites"
            } else {
                _message.value = "${product.title} is already a favorite"
            }
        }
    }
}