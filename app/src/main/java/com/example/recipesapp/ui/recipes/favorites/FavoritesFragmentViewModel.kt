package com.example.recipesapp.ui.recipes.favorites

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_BCG_FAVORITES
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesFragmentViewModel @Inject constructor(private val recipesRepository: RecipesRepository) : ViewModel() {

    data class FavoritesUiState(
        val favoritesImage: Drawable? = null,
        val favoritesList: List<Recipe>? = null,
        val isError: Boolean = true
    )

    private val mutableUIState = MutableLiveData<FavoritesUiState>()
    val uiState: LiveData<FavoritesUiState> get() = mutableUIState
    fun loadFavorites() {
        viewModelScope.launch {
            val drawable = recipesRepository.getDrawableAsset(IMAGE_BCG_FAVORITES)
            val recipeCache = recipesRepository.getFavoritesFromCache()
            mutableUIState.value = FavoritesUiState(
                favoritesList = recipeCache,
                favoritesImage = drawable,
                isError = recipeCache.isEmpty()
            )
        }
    }
}