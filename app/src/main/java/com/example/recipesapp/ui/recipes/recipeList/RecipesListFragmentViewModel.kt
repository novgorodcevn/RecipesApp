package com.example.recipesapp.ui.recipes.recipeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListFragmentViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    data class RecipesUiState(
        val imageUrl: String? = null,
        val tvHeading: String? = null,
        val recipesList: List<Recipe>? = null,
        val isError: Boolean = false,
    )

    private val mutableUIState = MutableLiveData<RecipesUiState>()
    val uiState: LiveData<RecipesUiState> get() = mutableUIState

    fun loadRecipe(recipeId: Int?, recipeName: String?, recipeImage: String?) {
        viewModelScope.launch {
            val recipesCache = recipesRepository.getRecipesFromCache(recipeId)
            val imageUrl = "$IMAGE_URL$recipeImage"
            if (recipeId != null) {

                mutableUIState.value = RecipesUiState(
                    imageUrl = imageUrl,
                    recipesList = recipesCache,
                    tvHeading = recipeName,
                )
                val recipesByCategoryId = recipesRepository.getRecipesByCategoryId(recipeId)
                val currentState = mutableUIState.value ?: RecipesUiState()
                recipesByCategoryId?.map { newRecipesByCategoryId ->
                    val recipeIdCache =
                        recipesRepository.getRecipeIdFromCache(newRecipesByCategoryId.id)
                    newRecipesByCategoryId.copy(
                        categoryId = recipeId,
                        isFavorite = recipeIdCache?.isFavorite ?: false
                    )
                }?.let {
                    recipesRepository.saveRecipesToCache(it)
                    mutableUIState.value = currentState.copy(
                        recipesList = it,
                        isError = false
                    )
                }
                if (recipesByCategoryId == null && recipesCache.isEmpty()) {
                    mutableUIState.value = currentState.copy(isError = true)
                }
            }
        }
    }
}