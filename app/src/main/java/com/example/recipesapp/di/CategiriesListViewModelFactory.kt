package com.example.recipesapp.di

import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.ui.category.CategoriesListFragmentViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListFragmentViewModel> {

    override fun create(): CategoriesListFragmentViewModel {
        return CategoriesListFragmentViewModel(recipesRepository)
    }
}