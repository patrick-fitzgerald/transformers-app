package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.response.Transformer

@Database(
    entities = [
        Transformer::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TransformersDatabase : RoomDatabase() {

    abstract fun transformerDao(): TransformerDao
}