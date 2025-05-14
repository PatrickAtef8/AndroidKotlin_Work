package com.example.jsonproductscoroutines

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    fun getProducts(): Call<ProductResponse>
}