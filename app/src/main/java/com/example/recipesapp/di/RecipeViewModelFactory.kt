package com.example.recipesapp.di

import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.ui.recipes.recipe.RecipeFragmentViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipeFragmentViewModel> {

    override fun create(): RecipeFragmentViewModel {
        return RecipeFragmentViewModel(recipesRepository)
    }
}