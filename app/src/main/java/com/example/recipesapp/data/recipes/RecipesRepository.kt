package com.example.recipesapp.data.recipes

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import com.example.recipesapp.data.category.CategoriesDao
import com.example.recipesapp.data.favorites.FavoritesRecipeDao
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val favoritesDao: FavoritesRecipeDao,
    private val service: RecipeApiService,
    private val asset: AssetManager,
) {
    suspend fun getDrawableAsset(drawable: String): Drawable? {
        return withContext(Dispatchers.IO) {
            try {
                asset.open(
                    drawable
                )
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: Exception) {
                null
            }
        }
    }

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