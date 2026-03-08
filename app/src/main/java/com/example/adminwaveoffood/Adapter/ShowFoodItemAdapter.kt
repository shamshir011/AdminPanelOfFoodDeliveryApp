package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.ManageCategoriesLayoutDesignBinding
import com.example.adminwaveoffood.model.AddItem

class ShowFoodItemAdapter(
    private val context: Context,
    private val foodItemList: ArrayList<AddItem>,
    private val onDeleteClickListener: (position: Int) -> Unit

) : RecyclerView.Adapter<ShowFoodItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ManageCategoriesLayoutDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return foodItemList.size
    }

    inner class ViewHolder(private val binding: ManageCategoriesLayoutDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            val item = foodItemList[position]

            binding.textViewCategory.text = item.title

            Glide.with(context)
                .load(Uri.parse(item.itemImage))
                .into(binding.imageViewCategory)

            binding.btnDelete.setOnClickListener {
                onDeleteClickListener(position)
            }
        }
    }

    fun removeItem(position: Int) {
        foodItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, foodItemList.size)
    }
}