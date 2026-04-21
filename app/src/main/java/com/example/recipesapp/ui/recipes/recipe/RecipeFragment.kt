package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.IngredientsAdapter
import com.example.recipesapp.MethodAdapter
import com.example.recipesapp.R
import com.example.recipesapp.RecipeApplication
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

    private lateinit var viewModel: RecipeFragmentViewModel
    private var _binding: FragmentRecipeBinding? = null

    private val args: RecipeFragmentArgs by navArgs()

    private lateinit var customAdapterIngredients: IngredientsAdapter
    private lateinit var customAdapterMethod: MethodAdapter
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        viewModel = appContainer.recipeViewModelFactory.create()
    }

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
            Glide.with(this)
                .load(uiState.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipes)
            binding.tvQuantityPortions.text = uiState.portions.toString()
            binding.tvHeadingCategories.text = uiState.tvHeading

            customAdapterIngredients.updateList(uiState.ingredientsList ?: emptyList())
            customAdapterMethod.updateList(uiState.methodList ?: emptyList())
            customAdapterIngredients.updateIngredients(uiState.portions)
            if (uiState.isError) {
                Toast.makeText(context, R.string.data_error, Toast.LENGTH_SHORT).show()
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