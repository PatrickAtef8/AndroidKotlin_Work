package com.example.jsonproductsdatabinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide



    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.context)
            .load(url)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(view)
    }
