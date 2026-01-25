package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.constants.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentRecipeBinding
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
    }

    private fun initUI() {
        binding.tvHeadingCategories.text = recipe?.title
        val image = recipe?.imageUrl?.let { context?.assets?.open(it) }.use { inputStream ->
            Drawable.createFromStream(inputStream, null)
        }
        binding.ivRecipes.setImageDrawable(image)
    }
}