package com.example.recipesapp.data.recipes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE categoryId = :id")
    fun getAll(id: Int?): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipe: List<Recipe>)
}