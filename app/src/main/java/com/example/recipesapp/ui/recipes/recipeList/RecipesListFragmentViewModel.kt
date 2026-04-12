package com.example.recipesapp.ui.recipes.recipeList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListFragmentViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesUiState(
        val imageUrl: String? = null,
        val tvHeading: String? = null,
        val recipesList: List<Recipe>? = null,
        val isError: Boolean = false,
    )

    private val mutableUIState = MutableLiveData<RecipesUiState>()
    val uiState: LiveData<RecipesUiState> get() = mutableUIState
    private val recipesRepository = RecipesRepository(application)

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
                recipesByCategoryId?.map { it.copy(categoryId = recipeId) }?.let {
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