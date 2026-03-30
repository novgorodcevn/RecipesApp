package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.constants.KEY_FAVORITES_ID
import com.example.recipesapp.constants.SAVE_FAVORITES_ID
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import java.util.concurrent.Executors

class RecipeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    data class RecipeUiState(
        val recipe: Recipe? = null,
        val portions: Int = 1,
        val recipeImage: Drawable? = null,
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
    private val executor = Executors.newCachedThreadPool()
    val uiState: LiveData<RecipeUiState> get() = mutableUIState

    private var currentRecipeId: Int? = null

    fun loadRecipe(recipeId: Int?) {
        executor.submit {
            val recipeById = recipesRepository.getRecipeById(recipeId)
            // TODO: load from network
            if (recipeId != null) {
                val drawable = try {
                    recipeById?.imageUrl?.let {
                        getApplication<Application>().assets?.open(
                            it
                        )
                    }
                        .use { inputStream ->
                            Drawable.createFromStream(inputStream, null)
                        }
                } catch (e: Exception) {
                    Log.e("RecipeViewModel", "Ошибка загрузки изображения", e)
                    null
                }

                currentRecipeId = recipeId
                mutableUIState.postValue(
                        RecipeUiState(
                            recipe = recipeById,
                            isFavorite = favorites.contains(recipeId.toString()),
                            portions = mutableUIState.value?.portions ?: 1,
                            recipeImage = drawable,
                            ingredientsList = recipeById?.ingredients ?: emptyList(),
                            methodList = recipeById?.method ?: emptyList(),
                            tvHeading = recipeById?.title,
                            isError = recipeById == null
                        )
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