package com.example.repository

import androidx.lifecycle.LiveData
import com.example.api.Resource
import com.example.api.TransformersApi
import com.example.data.request.CreateTransformerRequest
import com.example.data.request.UpdateTransformerRequest
import com.example.data.response.Transformer
import com.example.data.response.TransformerListResponse
import com.example.db.TransformersDatabase
import timber.log.Timber

class TransformersRepository constructor(
    private val transformersApi: TransformersApi,
    private val transformersDatabase: TransformersDatabase
) {

    /**
     * Database methods
     */
    fun getTransformersFromDb(): LiveData<List<Transformer>> {
        return transformersDatabase.transformerDao().findTransformers()
    }

    /**
     * Network methods
     */

    suspend fun getTransformersFromApi(): Resource<TransformerListResponse> {
        return try {
            // API request
            val transformerListResponse = transformersApi.getTransformers()
            // DB insert
            transformersDatabase.transformerDao()
                .insertTransformers(transformerListResponse.transformers)

            Timber.d("getTransformersFromApi response=$transformerListResponse")
            Resource.success(data = transformerListResponse)
        } catch (exception: Exception) {
            Timber.e("getTransformersFromApi exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun getTransformer(transformerId: String): Resource<Transformer> {
        return try {
            val transformer = transformersApi.getTransformer(transformerId)
            Timber.d("getTransformer transformerId=$transformerId response=$transformer")
            Resource.success(data = transformer)
        } catch (exception: Exception) {
            Timber.e("getTransformers exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun deleteTransformer(transformerId: String): Resource<Transformer> {
        return try {
            // API request
            transformersApi.deleteTransformer(transformerId)
            // DB delete
            transformersDatabase.transformerDao().deleteTransformer(transformerId)
            Timber.d("deleteTransformer transformerId=$transformerId")
            Resource.success(data = null)
        } catch (exception: Exception) {
            Timber.e("deleteTransformer exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun putTransformer(updateTransformerRequest: UpdateTransformerRequest): Resource<Transformer> {
        return try {
            // API request
            val transformer = transformersApi.putTransformer(updateTransformerRequest)
            // DB insert
            transformersDatabase.transformerDao().insertTransformer(transformer)

            Timber.d("putTransformer response=$transformer")
            Resource.success(data = transformer)
        } catch (exception: Exception) {
            Timber.e("putTransformer exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun postTransformer(createTransformerRequest: CreateTransformerRequest): Resource<Transformer> {
        return try {
            // API request
            val transformer = transformersApi.postTransformer(createTransformerRequest)
            // DB insert
            transformersDatabase.transformerDao().insertTransformer(transformer)

            Timber.d("postTransformer response=$transformer")
            Resource.success(data = transformer)
        } catch (exception: Exception) {
            Timber.e("postTransformer exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }

    suspend fun getAllSpark(): Resource<String> {
        return try {
            val response = transformersApi.getAllSpark()
            Timber.d("getAllSpark response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("getAllSpark exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }
}
