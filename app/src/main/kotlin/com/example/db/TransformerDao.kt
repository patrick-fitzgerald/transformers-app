package com.example.db

import androidx.room.*
import com.example.data.response.Transformer

@Dao
interface TransformerDao {

    @Query("SELECT * FROM transformers")
    suspend fun getTransformers(): List<Transformer>

    @Query("SELECT * from transformers WHERE id= :id LIMIT 1")
    suspend fun findTransformer(id: String): Transformer

    @Insert
    suspend fun insertTransformer(transformer: Transformer)

    @Update
    suspend fun updateTransformer(transformer: Transformer)

    @Delete
    suspend fun deleteTransformer(transformer: Transformer)


}
