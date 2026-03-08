package com.example.recipesapp.ui.category

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.recipes.STUB
import com.example.recipesapp.model.Category

class CategoriesListFragmentViewModel(application: Application) : AndroidViewModel(application) {
    data class CategoriesUiState(
        val category: List<Category>? = null,
        val categoryImage: Drawable? = null,
    )

    private val mutableUIState = MutableLiveData<CategoriesUiState>()
    val uiState: LiveData<CategoriesUiState> get() = mutableUIState

    fun loadCategories() {
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
        mutableUIState.value = CategoriesUiState(
            category = STUB.getCategories(),
            categoryImage = drawable,
        )
    }
}