package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.RecipeApplication
import com.example.recipesapp.ui.recipes.recipeList.RecipesListAdapter
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null

    private val viewModel: FavoritesFragmentViewModel by viewModels()

    private lateinit var customAdapter: RecipesListAdapter
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadFavorites()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        customAdapter = RecipesListAdapter(emptyList())
        binding.rvFavorites.adapter = customAdapter
        customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.ivFavorites.setImageDrawable(uiState.favoritesImage)
            uiState.favoritesList?.let {
                if (it.isEmpty()) {
                    binding.rvFavorites.visibility = View.GONE
                    binding.tvEmptyFavorites.visibility = View.VISIBLE
                } else {
                    binding.rvFavorites.visibility = View.VISIBLE
                    binding.tvEmptyFavorites.visibility = View.GONE
                    customAdapter.updateList(uiState.favoritesList)
                }
            }
            if (uiState.isError) {
                Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId))
    }
}