package com.example.jsonproductsmvvm.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import com.example.jsonproductsmvvm.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): List<Product>

    @Insert
    suspend fun insert(product: Product)

    @Delete
   suspend fun delete(product: Product)
}