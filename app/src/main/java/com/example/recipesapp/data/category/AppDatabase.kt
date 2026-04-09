package com.example.recipesapp.data.category

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesapp.data.Converters
import com.example.recipesapp.data.recipes.RecipesDao
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

@TypeConverters(Converters::class)
@Database(entities = [Category::class, Recipe::class], version = 2,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao

    abstract fun recipesDao(): RecipesDao
}