package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.IngredientsAdapter
import com.example.recipesapp.MethodAdapter
import com.example.recipesapp.R
import com.example.recipesapp.constants.ARG_RECIPE_ID
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar?,
            progress: Int,
            fromUser: Boolean
        ) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    }

    private val viewModel: RecipeFragmentViewModel by viewModels()
    private var _binding: FragmentRecipeBinding? = null

    private val args: RecipeFragmentArgs by navArgs()

    private lateinit var customAdapterIngredients: IngredientsAdapter
    private lateinit var customAdapterMethod: MethodAdapter
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

        viewModel.loadRecipe(args.recipeId)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {

        customAdapterIngredients = IngredientsAdapter(emptyList())
        binding.rvIngredients.adapter = customAdapterIngredients

        customAdapterMethod = MethodAdapter(emptyList())
        binding.rvMethod.adapter = customAdapterMethod

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->

            if (uiState.isFavorite) {
                binding.ibFavorite.setImageResource(R.drawable.ic_heart)
            } else {
                binding.ibFavorite.setImageResource(R.drawable.ic_heart_empty)
            }
            binding.ivRecipes.setImageDrawable(uiState.recipeImage)
            binding.tvQuantityPortions.text = uiState.portions.toString()
            binding.tvHeadingCategories.text = uiState.tvHeading

            customAdapterIngredients.updateList(uiState.ingredientsList ?: emptyList())
            customAdapterMethod.updateList(uiState.methodList ?: emptyList())
            customAdapterIngredients.updateIngredients(uiState.portions)
            if (uiState.isError) {
                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
        binding.sbRecipe.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            viewModel.updatingPortions(
                progress
            )
        })
        binding.ibFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }
        val divider =
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ).apply {
                dividerInsetStart = resources.getDimensionPixelOffset(R.dimen.margin_big)
                dividerInsetEnd = resources.getDimensionPixelOffset(R.dimen.margin_big)
                isLastItemDecorated = false
                dividerColor = ContextCompat.getColor(requireContext(), R.color.nav_bar_color)
            }
        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)
    }
}