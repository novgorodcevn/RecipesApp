package com.example.recipesapp.ui.recipes.recipe

import com.example.recipesapp.model.Recipe

class RecipeFragmentViewModel {

    data class RecipeUiState (
        val recipe: Recipe? = null,
        val portions: Int = 1,
        val isFavorite: Boolean = false
    )
}