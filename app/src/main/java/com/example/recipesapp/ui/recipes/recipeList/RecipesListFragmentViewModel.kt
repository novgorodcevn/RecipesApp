package com.example.recipesapp.ui.recipes.recipeList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.category.CategoriesListFragmentViewModel.CategoriesUiState
import kotlinx.coroutines.launch

class RecipesListFragmentViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesUiState(
        val imageUrl: String? = null,
        val tvHeading: String? = null,
        val recipesList: List<Recipe>? = null,
        val isError: Boolean = true,
    )

    private val mutableUIState = MutableLiveData<RecipesUiState>()
    val uiState: LiveData<RecipesUiState> get() = mutableUIState
    private val recipesRepository = RecipesRepository(application)

    fun loadRecipe(recipeId: Int?, recipeName: String?, recipeImage: String?) {
        viewModelScope.launch {
            val recipesCache = recipesRepository.getRecipesFromCache(recipeId)
            val imageUrl = "$IMAGE_URL$recipeImage"
            if (recipeId != null) {
                val recipesByCategoryId = recipesRepository.getRecipesByCategoryId(recipeId)
                mutableUIState.value = RecipesUiState(
                    imageUrl = imageUrl,
                    recipesList = recipesCache,
                    tvHeading = recipeName,
                    isError = recipesByCategoryId == null
                )
                recipesByCategoryId?.let { recipesRepository.saveRecipesToCache(it) }
                val currentState = mutableUIState.value ?: RecipesUiState()
                recipesByCategoryId?.let {
                    mutableUIState.value = currentState.copy(
                        recipesList = it
                    )
                }
            }
        }
    }
}