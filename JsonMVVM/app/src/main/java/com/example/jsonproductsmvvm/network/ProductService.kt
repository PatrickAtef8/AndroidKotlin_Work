package com.example.jsonproductsmvvm.network

import com.example.jsonproductsmvvm.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>
}