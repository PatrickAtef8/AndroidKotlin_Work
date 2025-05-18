package com.example.jsonproductsmvvm.allproducts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonproductsmvvm.R
import com.example.jsonproductsmvvm.model.Product

class AllProductsAdapter(private var products: MutableList<Product>, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<AllProductsAdapter.ProductViewHolder>() {

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)

        fun bind(product: Product) {
            titleTextView.text = product.title
            itemView.setOnClickListener { listener.onProductClick(product) }
        }
    }
}