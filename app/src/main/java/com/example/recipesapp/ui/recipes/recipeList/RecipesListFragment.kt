package com.example.recipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.constants.ARG_CATEGORY_ID
import com.example.recipesapp.constants.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.constants.ARG_CATEGORY_NAME
import com.example.recipesapp.constants.ARG_RECIPE_ID
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import kotlin.getValue

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null

    private var argCategoryId: Int? = null
    private var argCategoryName: String? = null
    private var argCategoryImageUrl: String? = null

    private val viewModel: RecipesListFragmentViewModel by viewModels()

    private lateinit var customAdapter: RecipesListAdapter

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
        viewModel.loadRecipe(argCategoryId,argCategoryName,argCategoryImageUrl)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        customAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = customAdapter
        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.tvHeadingRecipes.text = uiState.tvHeading
            binding.ivRecipes.setImageDrawable(uiState.recipeImage)
            customAdapter.updateList(uiState.recipesList ?: emptyList())
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            ARG_RECIPE_ID to recipeId
        )
        findNavController().navigate(R.id.recipeFragment,bundle)
    }
}