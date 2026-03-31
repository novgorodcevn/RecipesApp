package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.constants.KEY_FAVORITES_ID
import com.example.recipesapp.constants.SAVE_FAVORITES_ID
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeUiState(
        val recipe: Recipe? = null,
        val portions: Int = 1,
        val imageUrl: String? = null,
        val isFavorite: Boolean = false,
        val tvHeading: String? = null,
        val ingredientsList: List<Ingredient>? = null,
        val methodList: List<String>? = null,
        val isError: Boolean = true,
    )

    private val sharedPref =
        application.getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
    private val favorites = getFavorites()
    private val mutableUIState = MutableLiveData<RecipeUiState>()

    private val recipesRepository = RecipesRepository()
    val uiState: LiveData<RecipeUiState> get() = mutableUIState

    private var currentRecipeId: Int? = null

    fun loadRecipe(recipeId: Int?) {
        viewModelScope.launch {
            val recipeById = recipesRepository.getRecipeById(recipeId)
            if (recipeId != null) {
                val imageUrl = "$IMAGE_URL${recipeById?.imageUrl}"
                currentRecipeId = recipeId
                mutableUIState.value = RecipeUiState(
                    recipe = recipeById,
                    isFavorite = favorites.contains(recipeId.toString()),
                    portions = mutableUIState.value?.portions ?: 1,
                    imageUrl = imageUrl,
                    ingredientsList = recipeById?.ingredients ?: emptyList(),
                    methodList = recipeById?.method ?: emptyList(),
                    tvHeading = recipeById?.title,
                    isError = recipeById == null
                )
            }
        }
    }

    fun onFavoritesClicked() {
        val recipeId = currentRecipeId ?: return
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

    fun updatingPortions(portionsCount: Int) {
        val currentState = mutableUIState.value ?: RecipeUiState()
        mutableUIState.value = currentState.copy(portions = portionsCount)
    }

    private fun getFavorites(): MutableSet<String> {
        val newSetPref = sharedPref?.getStringSet(KEY_FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(newSetPref)
    }

    private fun saveFavorites(favorites: Set<String>) {
        sharedPref.edit {
            putStringSet(KEY_FAVORITES_ID, favorites)
        }
    }
}