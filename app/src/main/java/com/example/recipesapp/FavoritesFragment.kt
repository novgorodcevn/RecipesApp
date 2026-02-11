package com.example.recipesapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.RecipeFragment.Companion.KEY_FAVORITES_ID
import com.example.recipesapp.RecipeFragment.Companion.SAVE_FAVORITES_ID
import com.example.recipesapp.constants.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.recipes.STUB

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        val image = "bcg_favorites.png".let { context?.assets?.open(it) }.use { inputStream ->
            Drawable.createFromStream(inputStream, null)
        }
        binding.ivFavorites.setImageDrawable(image)
    }

    private fun initRecycler() {
        val favoritesIdList =
            STUB.getRecipesByIds(getFavorites().mapNotNull { it.toIntOrNull() }.toSet())
        if (favoritesIdList.isEmpty()) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyFavorites.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvEmptyFavorites.visibility = View.GONE

            val customAdapter = RecipesListAdapter(favoritesIdList)
            binding.rvFavorites.adapter = customAdapter
            customAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(
            ARG_RECIPE to recipe
        )
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPref =
            requireContext().getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
        val newSetPref = sharedPref?.getStringSet(KEY_FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(newSetPref)
    }
}

