package com.example.jsonproductslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson

class ActivityB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        val productId = intent.getIntExtra("product_id", 1)
        val workRequest = OneTimeWorkRequestBuilder<ProductWorker>().build()
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val productsJson = workInfo.outputData.getString("products")
                        val productsArray = Gson().fromJson(productsJson, Array<Products>::class.java)
                        val products: List<Products> = productsArray?.toList() ?: emptyList()
                        val product = products.find { it.id == productId }
                        if (product != null) {

                            val bundle = Bundle()
                            bundle.putInt("product_id", product.id)
                            bundle.putString("product_title", product.title)
                            bundle.putString("product_description", product.description)


                            val fragmentB = FragmentB()
                            fragmentB.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_b_container, fragmentB)
                                .commit()

                        }
                    }
                    else -> {}
                }
            }
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}