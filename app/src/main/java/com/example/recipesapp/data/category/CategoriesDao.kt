package com.example.recipesapp.data.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories")
   suspend  fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(category: List<Category>)
}