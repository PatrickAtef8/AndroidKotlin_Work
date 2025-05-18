package com.example.jsonproductsmvvm.db

import com.example.jsonproductsmvvm.model.Product

interface ProductsLocalDataSource {
    suspend fun getAllProducts(): List<Product>
    suspend fun insertProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}