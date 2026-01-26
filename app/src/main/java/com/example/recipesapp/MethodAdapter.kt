package com.example.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMethodBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MethodAdapter.ViewHolder, position: Int) {
        val method: String = dataSet[position]
        val indexMethod = "${position + 1}. $method"
        viewHolder.binding.tvMethod.text = indexMethod
    }

    override fun getItemCount() = dataSet.size
}