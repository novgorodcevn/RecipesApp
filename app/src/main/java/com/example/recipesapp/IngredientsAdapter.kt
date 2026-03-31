package com.example.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientsBinding
import com.example.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var quantity: Int = 1

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    fun updateList(dataSetNew: List<Ingredient>) {
        dataSet = dataSetNew
        notifyDataSetChanged()
    }
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
        val quantityText = ingredient.quantity.toBigDecimalOrNull()
            ?.multiply(BigDecimal(quantity))
            ?.setScale(2, RoundingMode.DOWN)
            ?.stripTrailingZeros()
            ?.toPlainString()
            ?: ingredient.quantity
        val quantityRecipe = "$quantityText ${ingredient.unitOfMeasure}"
        viewHolder.binding.tvDescriptionRecipe.text = ingredient.description
        viewHolder.binding.tvQuantityRecipe.text = quantityRecipe
    }

    override fun getItemCount() = dataSet.size
}