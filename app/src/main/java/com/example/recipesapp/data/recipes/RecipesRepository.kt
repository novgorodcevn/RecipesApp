package com.example.recipesapp.data.recipes

import android.util.Log
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class RecipesRepository {

    val contentType = "application/json".toMediaType()
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    fun getCategories(): List<Category>? {
        return try {
            service.getCategories().execute().body()
        } catch (e: Exception) {
            null
        }
    }
    fun getRecipesByCategoryId(id: Int?): List<Recipe>? {
        return try {
            service.getRecipesByCategoryId(id).execute().body()
        } catch (e: Exception) {
            null
        }
    }
    fun getRecipeById(id: Int?): Recipe? {
        return try {
            service.getRecipeById(id).execute().body()
        } catch (e: Exception) {

            null
        }
    }

    fun getRecipesByIds(idFavorites: Set<Int>) : List<Recipe>? {
        return try {
            service.getRecipesByIds(idFavorites).execute().body()
        } catch (e: Exception) {
            null
        }
    }
}
