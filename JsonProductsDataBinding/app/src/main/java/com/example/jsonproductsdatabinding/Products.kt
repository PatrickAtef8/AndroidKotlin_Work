package com.example.jsonproductsdatabinding

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Products(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String
)