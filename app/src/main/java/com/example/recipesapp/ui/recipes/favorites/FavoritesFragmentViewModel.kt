package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.constants.KEY_FAVORITES_ID
import com.example.recipesapp.constants.SAVE_FAVORITES_ID
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Recipe
import java.util.concurrent.Executors

class FavoritesFragmentViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesUiState(
        val favoritesImage: Drawable? = null,
        val favoritesList: List<Recipe>? = null,
    )

    private val mutableUIState = MutableLiveData<FavoritesUiState>()
    val uiState: LiveData<FavoritesUiState> get() = mutableUIState

    private val sharedPref =
        application.getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
    private val executor = Executors.newCachedThreadPool()
    private val recipesRepository = RecipesRepository()

    fun loadFavorites() {
        executor.submit {
            val recipeById =
                recipesRepository.getRecipesByIds(getFavorites().mapNotNull { it.toIntOrNull() }
                    .toSet())
            val drawable = try {
                "bcg_favorites.png".let {
                    getApplication<Application>().assets?.open(
                        it
                    )
                }
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Ошибка загрузки изображения", e)
                null
            }
            mutableUIState.postValue(
                FavoritesUiState(
                    favoritesList = recipeById,
                    favoritesImage = drawable
                )
            )
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val newSetPref = sharedPref?.getStringSet(KEY_FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(newSetPref)
    }
}