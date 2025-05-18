package com.example.jsonproductsmvvm.network

import android.util.Log
import com.example.jsonproductsmvvm.model.ProductsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsRemoteDataSourceImpl(private val productService: ProductService) : ProductsRemoteDataSource {
    override suspend fun fetchProducts(): Response<ProductsResponse> {
        val result =  withContext(Dispatchers.IO) {
            val response = productService.getProducts()
            if (response.isSuccessful) {
                val products = response.body()?.products ?: emptyList()
                Log.d("RemoteDataSource", "Fetched ${products.size} products from API")
            } else {
                Log.e("RemoteDataSource", "Failed to fetch products")
            }
            response
        }
        return result
    }
}