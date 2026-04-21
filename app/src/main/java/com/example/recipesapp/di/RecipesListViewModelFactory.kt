package com.example.recipesapp.di

import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.ui.recipes.recipeList.RecipesListFragmentViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipesListFragmentViewModel> {

    override fun create(): RecipesListFragmentViewModel {
        return RecipesListFragmentViewModel(recipesRepository)
    }
}