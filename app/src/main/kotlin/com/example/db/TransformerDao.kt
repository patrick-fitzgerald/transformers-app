package com.example.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.data.response.Transformer

@Dao
interface TransformerDao {

    @Query("SELECT * FROM transformers")
    fun findTransformers(): LiveData<List<Transformer>>

    @Query("SELECT * from transformers WHERE id= :id LIMIT 1")
    suspend fun findTransformer(id: String): Transformer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransformer(transformer: Transformer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransformers(transformers: List<Transformer>)

    @Update
    suspend fun updateTransformer(transformer: Transformer)

    @Delete
    suspend fun deleteTransformer(transformer: Transformer)


}
