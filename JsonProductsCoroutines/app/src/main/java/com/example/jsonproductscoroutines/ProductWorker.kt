package com.example.jsonproductscoroutines

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson


class ProductWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {

    override fun doWork(): Result {
        return try {
            val response = RetrofitClient.apiService.getProducts().execute()

            if (response.isSuccessful) {
                val products = response.body()?.products ?: emptyList()
                val productsJson = Gson().toJson(products)
                val outputData = workDataOf("products" to productsJson)
                Result.success(outputData)
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}