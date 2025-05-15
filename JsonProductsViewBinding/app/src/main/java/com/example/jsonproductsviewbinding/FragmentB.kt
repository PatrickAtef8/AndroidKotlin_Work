package com.example.jsonproductsviewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jsonproductsviewbinding.databinding.FragmentBBinding

class FragmentB : Fragment() {
    private lateinit var binding: FragmentBBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBBinding.inflate(inflater, container, false)
        val title = arguments?.getString("product_title") ?: "select product u want"
        val description = arguments?.getString("product_description") ?: ""
        binding.productName.text = title
        binding.productDescription.text = description
        return binding.root
    }

}