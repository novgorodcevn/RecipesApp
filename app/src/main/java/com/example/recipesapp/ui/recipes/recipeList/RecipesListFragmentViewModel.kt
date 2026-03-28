package com.example.recipesapp.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.recipes.RecipesRepository
import com.example.recipesapp.data.recipes.STUB
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragmentViewModel.RecipeUiState
import java.util.concurrent.Executors

class RecipesListFragmentViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesUiState(
        val recipeImage: Drawable? = null,
        val tvHeading: String? = null,
        val recipesList: List<Recipe>? = null,
        val isError: Boolean = true,
    )

    private val mutableUIState = MutableLiveData<RecipesUiState>()
    val uiState: LiveData<RecipesUiState> get() = mutableUIState
    private val recipesRepository = RecipesRepository()
    private val executor = Executors.newCachedThreadPool()
    fun loadRecipe(recipeId: Int?, recipeName: String?, recipeImage: String?) {
        // TODO: load from network
        executor.submit {
            if (recipeId != null) {
                val drawable = try {
                    recipeImage.let {
                        it?.let { fileName ->
                            getApplication<Application>().assets?.open(
                                fileName
                            )
                        }
                    }
                        .use { inputStream ->
                            Drawable.createFromStream(inputStream, null)
                        }
                } catch (e: Exception) {
                    Log.e("RecipeViewModel", "Ошибка загрузки изображения", e)
                    null
                }
                val recipesByCategoryId = recipesRepository.getRecipesByCategoryId(recipeId)
                mutableUIState.postValue(
                    RecipesUiState(
                        recipeImage = drawable,
                        recipesList = recipesByCategoryId,
                        tvHeading = recipeName,
                        isError = recipesByCategoryId == null
                    )
                )
            }
        }
    }
}