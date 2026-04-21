package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.constants.URL
import com.example.recipesapp.data.category.AppDatabase
import com.example.recipesapp.data.recipes.RecipeApiService
import com.example.recipesapp.data.recipes.RecipesRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration().build()

    private val recipesDao = db.recipesDao()
    private val categoriesDao = db.categoriesDao()
    private val favoritesDao = db.favoritesRecipeDao()
    private val drawableContext = context.assets
    val repository = RecipesRepository(
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        favoritesDao = favoritesDao,
        service = service,
        asset = drawableContext
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
}