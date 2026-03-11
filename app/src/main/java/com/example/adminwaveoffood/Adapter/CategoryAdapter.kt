package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffood.databinding.CheckboxCategoryItemRestaurantBinding

class CategoryAdapter(
    private val context: Context,
    private val categoryNames: ArrayList<String>,
    private val selectedCategories: HashMap<String, Boolean>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CheckboxCategoryItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int{
        return categoryNames.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: CheckboxCategoryItemRestaurantBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            val categoryName = categoryNames[position]

            binding.apply {

                cbCategory.text = categoryName

                // restore selected state
                cbCategory.isChecked = selectedCategories[categoryName] == true

                cbCategory.setOnCheckedChangeListener { _, isChecked ->

                    if (isChecked) {
                        selectedCategories[categoryName] = true
                    } else {
                        selectedCategories.remove(categoryName)
                    }

                }
            }
        }
    }
}