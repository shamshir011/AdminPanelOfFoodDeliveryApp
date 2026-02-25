package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.OrderDetailsActivity
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.OrderDetailItemBinding
import com.example.adminwaveoffood.databinding.OrdersItemBinding
import com.example.adminwaveoffood.model.OrderDetails

class OrderDetailsAdapter(
    private val context: Context,
    private val foodNames: ArrayList<String>,
    private val foodImages: ArrayList<String>,
    private val foodQuantities: ArrayList<Int>,
    private val foodPrices: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: OrderDetailItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            binding.apply {

                // Food name
                foodName.text = foodNames[position]

                // Quantity
                foodQuantity.text = foodQuantities[position].toString()

                // Price
                foodPrice.text = foodPrices[position]

                // Load image (Recommended: Glide)
                if (foodImages[position].isNotEmpty()) {

                    Glide.with(context)
                        .load(foodImages[position])
                        .placeholder(R.drawable.prifile_icon)
                        .into(cartImage)

                } else {
                    cartImage.setImageResource(R.drawable.prifile_icon)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = OrderDetailItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodNames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}