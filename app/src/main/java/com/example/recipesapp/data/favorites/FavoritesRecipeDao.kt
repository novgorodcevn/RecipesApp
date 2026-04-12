package com.example.recipesapp.data.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesRecipeDao {
    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    fun getAll(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipe: List<Recipe>)
}