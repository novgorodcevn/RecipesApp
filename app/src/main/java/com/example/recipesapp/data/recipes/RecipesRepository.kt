package com.example.recipesapp.data.recipes

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.recipesapp.constants.URL
import com.example.recipesapp.data.category.AppDatabase
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipesRepository(application: Application) {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration().build()

    private val recipesDao = db.recipesDao()
    private val categoriesDao = db.categoriesDao()

    private val favoritesDao = db.favoritesRecipeDao()


    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(Dispatchers.IO) { categoriesDao.getAll() }
    }

    suspend fun saveCategoriesToCache(category: List<Category>) {
        withContext(Dispatchers.IO) {
            categoriesDao.insertAll(category)
        }
    }

    suspend fun getRecipesFromCache(id: Int?): List<Recipe> {
        return withContext(Dispatchers.IO) { recipesDao.getAll(id) }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        withContext(Dispatchers.IO) {
            recipesDao.insertAll(recipes)
        }
    }

    suspend fun getFavoritesFromCache(): List<Recipe> {
        return withContext(Dispatchers.IO) { favoritesDao.getAll() }
    }

    suspend fun saveFavoritesToCache(recipes: Recipe) {
        withContext(Dispatchers.IO) {
            favoritesDao.insertAll(listOf(recipes))
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                service.getCategories().execute().body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeIdFromCache(id: Int?): Recipe? {
        return withContext(Dispatchers.IO) {
            recipesDao.getOne(id)
        }
    }

    suspend fun getRecipesByCategoryId(id: Int?): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipesByCategoryId(id).execute().body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeById(id: Int?): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipeById(id).execute().body()
            } catch (e: Exception) {
                null
            }
        }
    }
}