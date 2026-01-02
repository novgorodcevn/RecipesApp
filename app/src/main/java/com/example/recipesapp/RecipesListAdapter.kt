package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemRecipesBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
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

        val image = try {
            viewHolder.itemView.context.assets.open(recipe.imageUrl).use { inputStream ->
                Drawable.createFromStream(inputStream, null)
            }
        } catch (e: Exception) {
            Log.e("error", "Error loading image: ${recipe.imageUrl}", e)
            null
        }
        viewHolder.binding.ivItemRecipes.setImageDrawable(image)
    }

    override fun getItemCount() = dataSet.size
}