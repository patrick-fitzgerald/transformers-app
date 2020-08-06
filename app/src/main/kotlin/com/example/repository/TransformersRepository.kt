package com.example.repository

import com.example.api.TransformersApi
import com.example.api.Resource
import com.example.data.response.TransformerResponse
import timber.log.Timber

class TransformersRepository constructor(
    private val transformersApi: TransformersApi
) {

    suspend fun getAllSpark(): Resource<String> {
        return try {
            val response = transformersApi.getAllSpark()
            Timber.d("allSpark response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("allSpark exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun getTransformers(): Resource<List<TransformerResponse>> {
        return try {
            val response = transformersApi.getTransformers()
            Timber.d("getTransformers response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("getTransformers exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }
}
