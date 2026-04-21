package com.example.recipesapp.di

import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.ui.recipes.favorites.FavoritesFragmentViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoritesFragmentViewModel> {

    override fun create(): FavoritesFragmentViewModel {
        return FavoritesFragmentViewModel(recipesRepository)
    }
}
