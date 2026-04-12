package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_BCG_FAVORITES
import com.example.recipesapp.constants.KEY_FAVORITES_ID
import com.example.recipesapp.constants.SAVE_FAVORITES_ID
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesUiState(
        val favoritesImage: Drawable? = null,
        val favoritesList: List<Recipe>? = null,
        val isError: Boolean = true
    )

    private val mutableUIState = MutableLiveData<FavoritesUiState>()
    val uiState: LiveData<FavoritesUiState> get() = mutableUIState

    private val sharedPref =
        application.getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
    private val recipesRepository = RecipesRepository(application)

    fun loadFavorites() {

        val ids = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()
        val drawable = try {
            IMAGE_BCG_FAVORITES.let {
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
        viewModelScope.launch {
            val recipeById = recipesRepository.getRecipesByIds(ids)
            val recipeCache = recipesRepository.getFavoritesFromCache()
            recipeCache.collect() { recipes ->
                mutableUIState.value = FavoritesUiState(
                    favoritesList = recipes,
                    favoritesImage = drawable,
                    isError = recipeById == null
                )
            }
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val newSetPref = sharedPref?.getStringSet(KEY_FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(newSetPref)
    }
}