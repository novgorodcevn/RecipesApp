package com.example.recipesapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.IngredientsAdapter
import com.example.recipesapp.MethodAdapter
import com.example.recipesapp.R
import com.example.recipesapp.constants.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.example.recipesapp.model.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    private var recipe: Recipe? = null

    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE)
        }
        initUI()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                dividerInsetStart = resources.getDimensionPixelOffset(R.dimen.margin_big)
                dividerInsetEnd = resources.getDimensionPixelOffset(R.dimen.margin_big)
                isLastItemDecorated = false
                dividerColor = ContextCompat.getColor(requireContext(), R.color.nav_bar_color)
            }

        val customAdapterIngredients = IngredientsAdapter(recipe?.ingredients ?: emptyList())
        binding.rvIngredients.adapter = customAdapterIngredients
        binding.rvIngredients.addItemDecoration(divider)

        val customAdapterMethod = MethodAdapter(recipe?.method ?: emptyList())
        binding.rvMethod.adapter = customAdapterMethod
        binding.rvMethod.addItemDecoration(divider)
        binding.sbRecipe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.tvQuantityPortions.text = progress.toString()
                customAdapterIngredients.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initUI() {
        val favorites = getFavorites()
        val isFavorite = favorites.contains(recipe?.id.toString())
        if (isFavorite) {
            binding.ibFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            binding.ibFavorite.setImageResource(R.drawable.ic_heart_empty)
        }

        binding.ibFavorite.setOnClickListener {
            val currentFavorites = getFavorites()
            if (currentFavorites.contains(recipe?.id.toString())) {
                binding.ibFavorite.setImageResource(R.drawable.ic_heart_empty)
                currentFavorites.remove(recipe?.id.toString())
            } else {
                binding.ibFavorite.setImageResource(R.drawable.ic_heart)
                currentFavorites.add(recipe?.id.toString())
            }
            saveFavorites(currentFavorites)
        }
        binding.tvHeadingCategories.text = recipe?.title
        val image = recipe?.imageUrl?.let { context?.assets?.open(it) }.use { inputStream ->
            Drawable.createFromStream(inputStream, null)
        }
        binding.ivRecipes.setImageDrawable(image)
    }

    private fun saveFavorites(favorites: Set<String>) {
        val sharedPref = requireContext().getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
        sharedPref.edit {
            putStringSet(KEY_FAVORITES_ID, favorites)
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPref = requireContext().getSharedPreferences(SAVE_FAVORITES_ID, Context.MODE_PRIVATE)
        val newSetPref = sharedPref?.getStringSet(KEY_FAVORITES_ID, emptySet()) ?: emptySet()
        return HashSet(newSetPref)
    }

    companion object {
        const val KEY_FAVORITES_ID = "favorite_id"
        const val SAVE_FAVORITES_ID = "save_favorites"
    }
}