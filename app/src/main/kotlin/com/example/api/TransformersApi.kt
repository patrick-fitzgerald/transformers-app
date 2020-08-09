package com.example.api

import com.example.data.request.CreateTransformerRequest
import com.example.data.request.UpdateTransformerRequest
import com.example.data.response.Transformer
import com.example.data.response.TransformerListResponse
import retrofit2.Response
import retrofit2.http.*

interface TransformersApi {

    @GET("allspark")
    suspend fun getAllSpark(): String

    @GET("transformers")
    suspend fun getTransformers(): TransformerListResponse

    @POST("transformers")
    suspend fun postTransformer(@Body createTransformerRequest: CreateTransformerRequest): Transformer

    @PUT("transformers")
    suspend fun putTransformer(@Body updateTransformerRequest: UpdateTransformerRequest): Transformer

    @GET("transformers/{id}")
    suspend fun getTransformer(@Path("id") id: String): Transformer

    @DELETE("transformers/{id}")
    suspend fun deleteTransformer(@Path("id") id: String): Response<Unit>
}
