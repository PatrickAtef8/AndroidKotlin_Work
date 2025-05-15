package com.example.jsonproductsviewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentB : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_b, container, false)
        val nameTextView: TextView = view.findViewById(R.id.product_name)
        val descriptionTextView: TextView = view.findViewById(R.id.product_description)

        val title = arguments?.getString("product_title") ?: "select product u want"
        val description = arguments?.getString("product_description") ?: ""
        nameTextView.text = title
        descriptionTextView.text = description

        return view
    }
}