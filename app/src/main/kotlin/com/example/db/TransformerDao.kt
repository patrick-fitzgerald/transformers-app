package com.example.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.data.response.Transformer

@Dao
interface TransformerDao {

    @Query("SELECT * FROM transformers")
    fun findTransformers(): LiveData<List<Transformer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransformer(transformer: Transformer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransformers(transformers: List<Transformer>)

    @Query("DELETE FROM transformers WHERE id = :id")
    suspend fun deleteTransformer(id: String)


}
