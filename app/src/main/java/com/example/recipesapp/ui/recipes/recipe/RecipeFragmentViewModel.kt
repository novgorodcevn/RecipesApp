package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.constants.KEY_FAVORITES_ID
import com.example.recipesapp.constants.SAVE_FAVORITES_ID
import com.example.recipesapp.data.recipes.STUB
import com.example.recipesapp.model.Recipe

class RecipeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeUiState(
        val recipe: Recipe? = null,
        val portions: Int = 1,
        val isFavorite: Boolean = false
    )

    private val sharedPref =
        application.getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
    private val sharedPrefSave =
        application.getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
    private val favorites = getFavorites()
    private val mutableUIState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> get() = mutableUIState

    init {
        Log.d("InitRecipeView", "избранное = false")

    }

    fun loadRecipe(recipeId: Int?) {
        // TODO: load from network
        if (recipeId != null) {
            mutableUIState.value = RecipeUiState(recipe = STUB.getRecipeById(recipeId))
            val isFavorite = favorites.contains(recipeId.toString())
            if (isFavorite) {
                mutableUIState.value = RecipeUiState(isFavorite = true)
            } else {
                mutableUIState.value = RecipeUiState(isFavorite = false)
            }
            mutableUIState.value = RecipeUiState(portions = 1)
        }
    }

   fun onFavoritesClicked(recipeId: Int?) {
       val currentState = mutableUIState.value ?: RecipeUiState()
       val currentFavorites = getFavorites()
       if (currentFavorites.contains(recipeId.toString())) {
           mutableUIState.value = currentState.copy(isFavorite = false)
           currentFavorites.remove(recipeId.toString())
       } else {
           mutableUIState.value = currentState.copy(isFavorite = true)
           currentFavorites.add(recipeId.toString())
       }
       saveFavorites(currentFavorites)
    }

    private fun getFavorites(): MutableSet<String> {
        val newSetPref = sharedPref?.getStringSet(KEY_FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(newSetPref)
    }

    private fun saveFavorites(favorites: Set<String>) {
        sharedPrefSave.edit {
            putStringSet(KEY_FAVORITES_ID, favorites)
        }
    }
}