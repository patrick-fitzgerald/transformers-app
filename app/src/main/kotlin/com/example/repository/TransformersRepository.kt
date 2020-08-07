package com.example.repository

import androidx.lifecycle.LiveData
import com.example.api.Resource
import com.example.api.TransformersApi
import com.example.data.request.TransformerRequest
import com.example.data.response.Transformer
import com.example.data.response.TransformerListResponse
import com.example.db.TransformersDatabase
import timber.log.Timber

class TransformersRepository constructor(
    private val transformersApi: TransformersApi,
    private val transformersDatabase: TransformersDatabase
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

    suspend fun getTransformersFromApi(): Resource<TransformerListResponse> {
        return try {
            // API request
            val transformerListResponse = transformersApi.getTransformers()
            // DB insert
            transformersDatabase.transformerDao().insertTransformers(transformerListResponse.transformers)

            Timber.d("getTransformers response=$transformerListResponse")
            Resource.success(data = transformerListResponse)
        } catch (exception: Exception) {
            Timber.e("getTransformers exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    fun getTransformersFromDb(): LiveData<List<Transformer>> {
        return transformersDatabase.transformerDao().findTransformers()
    }

    suspend fun postTransformer(transformerRequest: TransformerRequest): Resource<Transformer> {
        return try {
            // API request
            val transformer = transformersApi.postTransformer(transformerRequest)
            // DB insert
            transformersDatabase.transformerDao().insertTransformer(transformer)

            Timber.d("postTransformer response=$transformer")
            Resource.success(data = transformer)
        } catch (exception: Exception) {
            Timber.e("postTransformer exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }
}
