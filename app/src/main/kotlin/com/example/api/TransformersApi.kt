package com.example.api

import com.example.data.response.TransformerResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TransformersApi {

    @GET("allspark")
    suspend fun getAllSpark(): String

    @GET("transformers")
    suspend fun getTransformers(): List<TransformerResponse>

    @POST("transformers")
    suspend fun postTransformer(): TransformerResponse

    @PUT("transformers")
    suspend fun putTransformer(): TransformerResponse

    @GET("transformers/id")
    suspend fun getTransformer(): TransformerResponse

    @DELETE("transformers/id")
    suspend fun deleteTransformer(): TransformerResponse
}
