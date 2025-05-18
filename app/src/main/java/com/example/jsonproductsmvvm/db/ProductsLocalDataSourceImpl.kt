package com.example.jsonproductsmvvm.db

import android.util.Log
import com.example.jsonproductsmvvm.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsLocalDataSourceImpl(private val productDao: ProductDao) : ProductsLocalDataSource {
    override suspend fun getAllProducts(): List<Product> {
        val result = withContext(Dispatchers.IO)
        {
            val products = productDao.getAll()
            Log.d("LocalDataSource", "Retrieved ${products.size} products from database")
            products
        }
        return result
    }


    override suspend fun insertProduct(product: Product) {

        withContext(Dispatchers.IO)
        {
            productDao.insert(product)
            Log.d("LocalDataSource", "Inserted product: ${product.title}")
        }

    }

    override suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO)
        {
            productDao.delete(product)
            Log.d("LocalDataSource", "Deleted product: ${product.title}")
        }

    }
}