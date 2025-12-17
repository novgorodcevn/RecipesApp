package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCategoryBinding.bind(itemView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]
        viewHolder.binding.titleItemCategory.text = category.title
        viewHolder.binding.descriptionItemCategory.text = category.description
        val inputStrim = viewHolder.itemView.context.assets.open(category.imageUrl)
        val image = Drawable.createFromStream(inputStrim, null)
        viewHolder.binding.imageItemCategory.setImageDrawable(image)
     //  val inputStream = context.getAssets().open(url)
       // val d = Drawable.createFromStream(context?.assets?.open("imageData/${imageName}.png"), null)
    }

    override fun getItemCount() = dataSet.size

}