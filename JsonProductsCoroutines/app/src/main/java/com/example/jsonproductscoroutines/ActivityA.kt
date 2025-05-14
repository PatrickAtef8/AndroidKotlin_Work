package com.example.jsonproductscoroutines

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson

class ActivityA : AppCompatActivity() {

    private var products: List<Products> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_a_container, FragmentA())
                .commit()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentBContainer = findViewById<View>(R.id.fragment_b_container)
            if (fragmentBContainer != null) {
                fragmentBContainer.visibility = View.VISIBLE


                val fragmentB = FragmentB()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_b_container, fragmentB, "FragmentB")
                    .commit()


            } else {
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<ProductWorker>().build()
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        val productsJson = workInfo.outputData.getString("products")
                        val productsArray = Gson().fromJson(productsJson, Array<Products>::class.java)
                        products = productsArray?.toList() ?: emptyList()
                    }
                    else -> {}
                }
            }
        WorkManager.getInstance(this).enqueue(workRequest)
    }



    fun showProductDetails(productId: Int) {
        val product = products.find { it.id == productId }
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val fragmentBContainer = findViewById<View>(R.id.fragment_b_container)
            if (fragmentBContainer != null && product != null) {


                val bundle = Bundle()
                bundle.putInt("product_id", product.id)
                bundle.putString("product_title", product.title)
                bundle.putString("product_description", product.description)


                val fragmentB = FragmentB()
                fragmentB.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_b_container, fragmentB, "FragmentB")
                    .addToBackStack(null)
                    .commit()



            } else {
                val intent = Intent(this, ActivityB::class.java)
                intent.putExtra("product_id", productId)
                startActivity(intent)
            }
        } else {
            val intent = Intent(this, ActivityB::class.java)
            intent.putExtra("product_id", productId)
            startActivity(intent)
        }
    }
}