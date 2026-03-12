package com.example.recipesapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
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
        val category = viewModel.getCategory(categoryId) ?: throw IllegalArgumentException()
        category.let {
            findNavController().navigate(
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    it
                )
            )
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
            customAdapter.updateList(uiState.category ?: emptyList())
        }
    }
}