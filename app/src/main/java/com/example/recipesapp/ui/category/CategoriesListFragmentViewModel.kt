package com.example.recipesapp.ui.category

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_BCG_CATEGORIES
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListFragmentViewModel @Inject constructor(private val recipesRepository: RecipesRepository) :
    ViewModel() {
    data class CategoriesUiState(
        val category: List<Category>? = null,
        val categoryImage: Drawable? = null,
        val isError: Boolean = false
    )

    private val mutableUIState = MutableLiveData<CategoriesUiState>()
    val uiState: LiveData<CategoriesUiState> get() = mutableUIState

    fun loadCategories() {

        viewModelScope.launch {
            val drawable = recipesRepository.getDrawableAsset(IMAGE_BCG_CATEGORIES)
            val categoryCache = recipesRepository.getCategoriesFromCache()
            mutableUIState.value = CategoriesUiState(
                categoryImage = drawable,
                category = categoryCache,
            )
            val category = recipesRepository.getCategories()
            val currentState = mutableUIState.value ?: CategoriesUiState()
            category?.let {
                recipesRepository.saveCategoriesToCache(it)
                mutableUIState.value = currentState.copy(
                    category = it,
                    isError = false
                )
            }
            if (category == null && categoryCache.isEmpty()) {
                mutableUIState.value = currentState.copy(isError = true)
            }
        }
    }

    fun getCategory(categoryId: Int): Category? {
        return mutableUIState.value?.category?.find { it.id == categoryId }
    }
}