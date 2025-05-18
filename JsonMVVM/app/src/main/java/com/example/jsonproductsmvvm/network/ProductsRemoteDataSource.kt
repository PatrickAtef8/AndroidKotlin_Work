package com.example.jsonproductsmvvm.network

import com.example.jsonproductsmvvm.model.ProductsResponse
import retrofit2.Response

interface ProductsRemoteDataSource {
    suspend fun fetchProducts(): Response<ProductsResponse>
}