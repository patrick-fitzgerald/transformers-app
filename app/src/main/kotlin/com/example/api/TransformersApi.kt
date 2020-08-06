package com.example.api

import com.example.data.request.TransformerRequest
import com.example.data.response.TransformerListResponse
import com.example.data.response.TransformerResponse
import retrofit2.http.*

interface TransformersApi {

    @GET("allspark")
    suspend fun getAllSpark(): String

    @GET("transformers")
    suspend fun getTransformers(): TransformerListResponse


    @POST("transformers")
    suspend fun postTransformer(@Body transformerRequest: TransformerRequest): TransformerResponse

    @PUT("transformers")
    suspend fun putTransformer(@Body transformerRequest: TransformerRequest): TransformerResponse

    @GET("transformers/{id}")
    suspend fun getTransformer(@Path("id") id: String): TransformerResponse

    @DELETE("transformers/{id}")
    suspend fun deleteTransformer(@Path("id") id: String)
}
