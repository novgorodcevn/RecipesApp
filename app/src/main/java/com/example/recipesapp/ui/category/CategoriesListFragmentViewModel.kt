package com.example.recipesapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.data.recipes.STUB
import com.example.recipesapp.model.Category
import java.util.concurrent.Executors

class CategoriesListFragmentViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesUiState(
        val category: List<Category>? = null,
        val categoryImage: Drawable? = null,
        val isError: Boolean = true
    )

    private val recipesRepository = RecipesRepository()
    private val mutableUIState = MutableLiveData<CategoriesUiState>()
    val uiState: LiveData<CategoriesUiState> get() = mutableUIState
    private val executor = Executors.newCachedThreadPool()
    fun loadCategories() {
        executor.submit {
            val drawable = try {
                "bcg_categories.png".let {
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
            val category = recipesRepository.getCategories()
            mutableUIState.postValue(
                CategoriesUiState(
                    categoryImage = drawable,
                    category = category,
                    isError = category == null
                )
            )
        }

    }

    fun getCategory(categoryId: Int): Category? {
        return  STUB.getCategories().find { it.id == categoryId }
    }
}