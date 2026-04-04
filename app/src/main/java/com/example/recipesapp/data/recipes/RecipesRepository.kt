package com.example.recipesapp.data.recipes

import android.util.Log
import com.example.recipesapp.constants.URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class RecipesRepository {

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                service.getCategories().execute().body()
            } catch (e: Exception) {
                null
            }
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

    suspend fun getRecipesByIds(ids: Set<Int>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val idsString = ids.joinToString(",")
                service.getRecipesByIds(idsString).execute().body()
            } catch (e: Exception) {
                null
            }
        }
    }
}