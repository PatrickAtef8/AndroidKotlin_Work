package com.example.jsonproductsmvvm.model

import com.example.jsonproductsmvvm.db.ProductsLocalDataSource
import com.example.jsonproductsmvvm.network.ProductsRemoteDataSource

class ProductsRepositoryImpl(
    private val remoteDataSource: ProductsRemoteDataSource,
    private val localDataSource: ProductsLocalDataSource
) : ProductsRepository {

    override suspend fun getProducts(fromRemote: Boolean): List<Product> {
        return if (fromRemote) {
            val response = remoteDataSource.fetchProducts()
            if (response.isSuccessful) {
                response.body()?.products ?: emptyList()
            } else {
                emptyList()
            }
        } else {
            localDataSource.getAllProducts()
        }
    }

    override suspend fun addFavorite(product: Product) {
        val updatedProduct = product.copy()
        localDataSource.insertProduct(updatedProduct)
    }

    override suspend fun removeFavorite(product: Product) {
        localDataSource.deleteProduct(product)
    }

    override suspend fun getFavorites(): List<Product> {
        return localDataSource.getAllProducts().filter { it.id > 0 }
    }


    companion object {
        private var instance: ProductsRepositoryImpl? = null

        fun getInstance(
            remoteDataSource: ProductsRemoteDataSource,
            localDataSource: ProductsLocalDataSource
        ): ProductsRepositoryImpl {
            return instance ?: synchronized(this) {
                val temp = ProductsRepositoryImpl(remoteDataSource, localDataSource)
                instance = temp
                temp
            }
        }
    }
}