package com.example.jsonproductscoroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson

class FragmentA : Fragment() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)
        recyclerView = view.findViewById(R.id.product_list)

        productAdapter = ProductAdapter { product ->
            (activity as? ActivityA)?.showProductDetails(product.id)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = productAdapter

        val workRequest = OneTimeWorkRequestBuilder<ProductWorker>().build()
        val workManager = WorkManager.getInstance(requireContext())
        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(viewLifecycleOwner) { workInfo ->
            when (workInfo?.state) {
                WorkInfo.State.SUCCEEDED -> {
                    val productsJson = workInfo.outputData.getString("products")
                    val productsArray = Gson().fromJson(productsJson, Array<Products>::class.java)
                    val products: List<Products> = productsArray?.toList() ?: emptyList()
                    productAdapter.submitList(products)
                }
                else -> {}
            }
        }
        workManager.enqueue(workRequest)

        return view
    }


}