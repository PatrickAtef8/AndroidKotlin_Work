package com.example.jsonproductscoroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentA : Fragment() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)
        recyclerView = view.findViewById(R.id.product_list)
        database = AppDatabase.getDatabase(requireContext())

        productAdapter = ProductAdapter { product ->
            (activity as? ActivityA)?.showProductDetails(product.id)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = productAdapter

        lifecycleScope.launch(Dispatchers.IO){
            try {
                val products = if (NetworkUtils.isNetworkConnected(requireContext())) {
                    val response = RetrofitClient.apiService.getProducts()
                    if (response.isSuccessful) {
                        response.body()?.products?.also {
                            database.productDao().clearAll()
                            database.productDao().insertAll(it)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Products loaded from api",Toast.LENGTH_SHORT).show() } } ?: emptyList()
                    } else {
                        database.productDao().getAllProducts().also {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Network error, loaded from room", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    database.productDao().getAllProducts().also {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Offline, loaded from room", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    productAdapter.submitList(products)
                }
            } catch (e: Exception) {
                val cachedProducts = database.productDao().getAllProducts()
                withContext(Dispatchers.Main) {
                    productAdapter.submitList(cachedProducts)
                    Toast.makeText(requireContext(), "Error: ${e.message}, loaded from room", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }


}