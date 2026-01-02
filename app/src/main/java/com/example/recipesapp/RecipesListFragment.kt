package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.constants.ARG_CATEGORY_ID
import com.example.recipesapp.constants.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.constants.ARG_CATEGORY_NAME
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.recipes.STUB

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null

    private var argCategoryId: Int? = null
    private var argCategoryName: String? = null
    private var argCategoryImageUrl: String? = null

    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        argCategoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        argCategoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        argCategoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)

        binding.tvHeadingRecipes.text = argCategoryName
        val image = argCategoryImageUrl?.let { context?.assets?.open(it) }.use { inputStream ->
            Drawable.createFromStream(inputStream, null)
        }
        binding.ivRecipes.setImageDrawable(image)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val customAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(argCategoryId))
        binding.rvRecipes.adapter = customAdapter
        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}