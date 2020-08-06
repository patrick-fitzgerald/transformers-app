package com.example.data.response

import com.google.gson.annotations.SerializedName

data class TransformerListResponse(

    @SerializedName("transformers")
    val transformers: List<TransformerResponse>
)
