package com.example.jsonproductsmvvm.favproducts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonproductsmvvm.model.Product
import com.example.jsonproductsmvvm.model.ProductsRepository
import kotlinx.coroutines.launch

class FavProductsViewModel(private val repository: ProductsRepository) : ViewModel() {
    private val _favorites = MutableLiveData<List<Product>>()
    val favorites: LiveData<List<Product>> = _favorites

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = repository.getFavorites()
            _favorites.value = favorites
        }
    }

    fun deleteFavorite(product: Product) {
        viewModelScope.launch {
            val existingFavorites = repository.getFavorites()
            if (existingFavorites.any { it.id == product.id }) {
                repository.removeFavorite(product)
                val updatedFavorites = repository.getFavorites() // Refresh the list
                _favorites.value = updatedFavorites
                _message.value = "${product.title} removed from favorites"
            } else {
                _message.value = "${product.title} not found in favorites"
            }
        }
    }
}