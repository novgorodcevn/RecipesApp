package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentListRecipesBinding

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

        argCategoryId = requireArguments().getInt("ARG_CATEGORY_ID")
        argCategoryName = requireArguments().getString("ARG_CATEGORY_NAME")
        argCategoryImageUrl = requireArguments().getString("ARG_CATEGORY_IMAGE_URL")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}