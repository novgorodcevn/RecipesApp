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
        val isError: Boolean = true,
    )

    private val mutableUIState = MutableLiveData<RecipesUiState>()
    val uiState: LiveData<RecipesUiState> get() = mutableUIState
    private val recipesRepository = RecipesRepository()

    fun loadRecipe(recipeId: Int?, recipeName: String?, recipeImage: String?) {
        viewModelScope.launch {
              val imageUrl = "$IMAGE_URL$recipeImage"
              if (recipeId != null) {
                  val recipesByCategoryId = recipesRepository.getRecipesByCategoryId(recipeId)
                  mutableUIState.value = RecipesUiState(
                      imageUrl = imageUrl,
                      recipesList = recipesByCategoryId,
                      tvHeading = recipeName,
                      isError = recipesByCategoryId == null
                  )
              }
          }
    }
}