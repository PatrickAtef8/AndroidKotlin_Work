package com.example.jsonproductsmvvm.favproducts.view

import com.example.jsonproductsmvvm.model.Product

interface OnFavoriteClickListener {
    fun onFavoriteClick(product: Product)
}