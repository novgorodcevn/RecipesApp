package com.example.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientsBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemIngredientsBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ingredients, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: IngredientsAdapter.ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        val quantityRecipe= "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        viewHolder.binding.tvDescriptionRecipe.text = ingredient.description
        viewHolder.binding.tvQuantityRecipe.text = quantityRecipe
    }

    override fun getItemCount() = dataSet.size
}