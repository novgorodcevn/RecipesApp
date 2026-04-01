package com.example.recipesapp.ui.recipes.recipeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.databinding.ItemRecipesBinding
import com.example.recipesapp.model.Recipe

class RecipesListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun updateList(dataSetNew: List<Recipe>) {
        dataSet = dataSetNew
        notifyDataSetChanged()
    }
    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecipesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recipes, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]
        viewHolder.binding.tvTitleItemRecipes.text = recipe.title
        viewHolder.binding.cwItemRecipes.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }

        val imageUrl = "$IMAGE_URL${recipe.imageUrl}"

        Glide.with(viewHolder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.binding.ivItemRecipes)
    }

    override fun getItemCount() = dataSet.size
}