package com.example.jsonproductsmvvm.favproducts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jsonproductsmvvm.R
import com.example.jsonproductsmvvm.model.Product

class FavProductsAdapter(private var favorites: MutableList<Product>, private val listener: OnFavoriteClickListener) :
    RecyclerView.Adapter<FavProductsAdapter.FavoriteViewHolder>() {

    fun updateFavorites(newFavorites: List<Product>) {
        favorites.clear()
        favorites.addAll(newFavorites)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorites, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount() = favorites.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.image_view_thumbnail)
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)

        fun bind(product: Product) {
            titleTextView.text = product.title
            descriptionTextView.text = product.description
            Glide.with(itemView.context)
                .load(product.thumbnail)

                .into(thumbnailImageView)
            itemView.setOnClickListener { listener.onFavoriteClick(product) }
        }
    }
}