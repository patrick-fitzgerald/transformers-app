package com.example.repository

import com.example.api.Resource
import com.example.api.TransformersApi
import com.example.data.request.TransformerRequest
import com.example.data.response.TransformerListResponse
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

    suspend fun getTransformers(): Resource<TransformerListResponse> {
        return try {
            val response = transformersApi.getTransformers()
            Timber.d("getTransformers response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("getTransformers exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun postTransformer(transformerRequest: TransformerRequest): Resource<TransformerResponse> {
        return try {
            val response = transformersApi.postTransformer(transformerRequest)
            Timber.d("postTransformer response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("postTransformer exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }
}
