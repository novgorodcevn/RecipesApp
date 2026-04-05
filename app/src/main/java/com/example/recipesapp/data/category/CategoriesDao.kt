package com.example.recipesapp.data.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(category: List<Category>)
}