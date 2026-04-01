package com.example.recipesapp.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.model.Category
import com.example.recipesapp.R
import com.example.recipesapp.constants.IMAGE_URL
import com.example.recipesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }
    fun updateList(dataSetNew: List<Category>) {
        dataSet = dataSetNew
        notifyDataSetChanged()
    }
    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

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
        viewHolder.binding.tvTitleItemCategory.text = category.title
        viewHolder.binding.tvDescriptionItemCategory.text = category.description
        viewHolder.binding.cwItemCategory.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
        val imageUrl = "$IMAGE_URL${category.imageUrl}"

        Glide.with(viewHolder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.binding.ivItemCategory)
    }

    override fun getItemCount() = dataSet.size
}