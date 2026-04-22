package com.example.recipesapp.di

import android.content.Context
import android.content.res.AssetManager
import androidx.room.Room
import com.example.recipesapp.constants.URL
import com.example.recipesapp.data.category.AppDatabase
import com.example.recipesapp.data.category.CategoriesDao
import com.example.recipesapp.data.favorites.FavoritesRecipeDao
import com.example.recipesapp.data.recipes.RecipeApiService
import com.example.recipesapp.data.recipes.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun providesCategoriesDao(appDataBase: AppDatabase): CategoriesDao = appDataBase.categoriesDao()

    @Provides
    fun providesRecipesDao(appDataBase: AppDatabase): RecipesDao = appDataBase.recipesDao()

    @Provides
    fun providesFavoritesDao(appDataBase: AppDatabase): FavoritesRecipeDao = appDataBase.favoritesRecipeDao()

    @Provides
    fun providesDrawableContext(@ApplicationContext context: Context): AssetManager = context.assets

    @Provides
    fun providesService(retrofit: Retrofit): RecipeApiService {
       return retrofit.create(RecipeApiService::class.java)
    }

    @Provides
    fun providesRetrofit(): Retrofit  {
         val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
     return retrofit
    }
}