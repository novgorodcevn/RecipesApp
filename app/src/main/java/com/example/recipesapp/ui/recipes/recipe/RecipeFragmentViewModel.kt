package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragmentViewModel.RecipeUiState

class RecipeFragmentViewModel : ViewModel() {

    data class RecipeUiState (
        val recipe: Recipe? = null,
        val portions: Int = 1,
        val isFavorite: Boolean = false
    )

    private val mutableUIState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> get() = mutableUIState

    init {
        Log.d("InitRecipeView","избранное = false")
        mutableUIState.value = RecipeUiState(isFavorite = false)
    }
}