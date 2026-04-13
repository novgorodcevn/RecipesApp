package com.example.recipesapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.constants.IMAGE_BCG_CATEGORIES
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListFragmentViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesUiState(
        val category: List<Category>? = null,
        val categoryImage: Drawable? = null,
        val isError: Boolean = false
    )

    private val recipesRepository = RecipesRepository(application)
    private val mutableUIState = MutableLiveData<CategoriesUiState>()
    val uiState: LiveData<CategoriesUiState> get() = mutableUIState

    fun loadCategories() {
        val drawable = try {
            IMAGE_BCG_CATEGORIES.let {
                getApplication<Application>().assets?.open(
                    it
                )
            }
                .use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
        } catch (e: Exception) {
            Log.e("CategoriesViewModel", "Ошибка загрузки изображения", e)
            null
        }
        viewModelScope.launch {
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