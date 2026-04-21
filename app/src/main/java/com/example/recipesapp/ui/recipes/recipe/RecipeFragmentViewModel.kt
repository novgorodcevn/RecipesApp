package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeFragmentViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {
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

    private val mutableUIState = MutableLiveData<RecipeUiState>()

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
                    isFavorite = recipesRepository.getRecipeIdFromCache(recipeId)?.isFavorite
                        ?: false,
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
        viewModelScope.launch {
            val recipeCache = recipesRepository.getRecipeIdFromCache(recipeId)
            if (recipeCache?.isFavorite == true) {
                currentState.recipe?.let {
                    recipesRepository.saveFavoritesToCache(
                        it.copy(
                            isFavorite = false
                        )
                    )
                }
                mutableUIState.value = currentState.copy(isFavorite = false)
            } else {
                currentState.recipe?.let {
                    recipesRepository.saveFavoritesToCache(
                        it.copy(
                            isFavorite = true
                        )
                    )
                }
                mutableUIState.value = currentState.copy(isFavorite = true)
            }
        }
    }

    fun updatingPortions(portionsCount: Int) {
        val currentState = mutableUIState.value ?: RecipeUiState()
        mutableUIState.value = currentState.copy(portions = portionsCount)
    }
}