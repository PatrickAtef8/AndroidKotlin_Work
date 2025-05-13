package com.example.dummyproductslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FragmentA : Fragment() {

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.product_list)

        productAdapter = ProductAdapter { product ->
            (activity as? ActivityA)?.showProductDetails(product.id)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = productAdapter

        productAdapter.submitList(dummyProducts)

        return view
    }
}