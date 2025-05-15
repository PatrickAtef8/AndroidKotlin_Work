package com.example.jsonproductsdatabinding

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jsonproductsdatabinding.databinding.ProductItemBinding

interface OnProductClickListener {
    fun onProductClick(product: Products)
}


class ProductAdapter(private val listener: OnProductClickListener) :
    ListAdapter<Products, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {


//private lateinit var binding : ProductItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
       val  binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    class ProductViewHolder(
        private var binding : ProductItemBinding,
        private val listener: OnProductClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Products) {
            binding.product = product
            binding.listener = listener
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Products>() {
    override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
        return oldItem == newItem
    }

}