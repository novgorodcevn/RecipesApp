package com.example.recipesapp.data.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesRecipeDao {
    @Query("SELECT * FROM recipe WHERE isFavorite = true")
    fun getAll(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipe: List<Recipe>)
}