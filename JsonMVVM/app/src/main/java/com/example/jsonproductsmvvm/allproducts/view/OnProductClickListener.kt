package com.example.jsonproductsmvvm.allproducts.view

import com.example.jsonproductsmvvm.model.Product

interface OnProductClickListener {
    fun onProductClick(product: Product)
}