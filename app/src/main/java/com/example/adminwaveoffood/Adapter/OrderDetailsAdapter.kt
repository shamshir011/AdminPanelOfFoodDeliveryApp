package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.OrderDetailItemBinding
import com.example.adminwaveoffood.databinding.OrdersItemBinding

class OrderDetailsAdapter(
    private val context: Context,
    private var foodNames: ArrayList<String>,
    private var foodQuantities: ArrayList<Int>,
    private var foodPrices: ArrayList<String>
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder{
        val binding = OrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }
    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int){
        holder.bind(position)
    }
    override fun getItemCount(): Int{
        return foodNames.size
    }

    inner class OrderDetailsViewHolder(private val binding: OrdersItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                textViewFoodName.text = foodNames[position]
                textViewQuantity.text = foodQuantities[position].toString()
                textViewPrice.text = foodPrices[position]

            }
        }

    }

}