package com.example.recipesapp.ui.category

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipesapp.IngredientsAdapter
import com.example.recipesapp.R
import com.example.recipesapp.ui.recipes.recipeList.RecipesListFragment
import com.example.recipesapp.constants.ARG_CATEGORY_ID
import com.example.recipesapp.constants.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.constants.ARG_CATEGORY_NAME
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.data.recipes.STUB
import kotlin.getValue

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null

    private val viewModel: CategoriesListFragmentViewModel by viewModels()

    private lateinit var customAdapter: CategoriesListAdapter

    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCategories()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.getCategory(categoryId)
        val categoryName = category?.title
        val categoryImageUrl = category?.imageUrl
        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )
        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun initUI() {
        customAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = customAdapter
        customAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.ivCategories.setImageDrawable(uiState.categoryImage)
            customAdapter.updateList(uiState.category?: emptyList())
        }
    }
}