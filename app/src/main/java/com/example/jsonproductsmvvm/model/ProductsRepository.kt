package com.example.jsonproductsmvvm.model

interface ProductsRepository {
    suspend fun getProducts(flag : Boolean): List<Product>
    suspend fun addFavorite(product: Product)
    suspend fun removeFavorite(product: Product)
    suspend fun getFavorites(): List<Product>
}