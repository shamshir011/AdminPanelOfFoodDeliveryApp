package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.Adapter.MenuItemAdapter.ViewHolder
import com.example.adminwaveoffood.databinding.ItemItemBinding
import com.example.adminwaveoffood.databinding.ManageCategoriesLayoutDesignBinding
import com.example.adminwaveoffood.model.AllMenu
import com.example.adminwaveoffood.model.FoodCategory
import com.google.firebase.database.DatabaseReference

class ShowCategoryAdapter(
    private val context: Context,
    private val categoryList: ArrayList<FoodCategory>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position: Int) -> Unit

): RecyclerView.Adapter<ShowCategoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = ManageCategoriesLayoutDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(position)
    }

    override fun getItemCount(): Int{
        return categoryList.size
    }

    inner class ViewHolder(private val binding: ManageCategoriesLayoutDesignBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val categoryItem = categoryList[position]
                val uriString = categoryItem.foodCategoryImage
                val uri = Uri.parse(uriString)
                textViewCategory.text = categoryItem.foodCategoryName
                Glide.with(context).load(uri).into(imageViewCategory)

                binding.btnDelete.setOnClickListener {
                    onDeleteClickListener(position)

//                    val itemPosition =  adapterPosition
//                    if(itemPosition != RecyclerView.NO_POSITION){
//                        deleteItem(position)
//                    }
                }
            }
        }

        fun deleteItem(position: Int){
            categoryList.removeAt(position)
            categoryList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, categoryList.size)
        }
    }
}