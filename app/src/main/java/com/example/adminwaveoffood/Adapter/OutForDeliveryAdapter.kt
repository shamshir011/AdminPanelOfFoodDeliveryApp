package com.example.adminwaveoffood.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffood.databinding.DeliveryItemBinding

class OutForDeliveryAdapter(
    private val customerName: MutableList<String>,
    private val moneyStatus: MutableList<Boolean>
): RecyclerView.Adapter<OutForDeliveryAdapter.OutForDeliveryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutForDeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OutForDeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OutForDeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return customerName.size
    }

    inner class OutForDeliveryViewHolder(private val binding: DeliveryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                textViewCustomerName.text = customerName[position]
                if(moneyStatus[position] == true){
                    paymentStatus.text = "Received"
                }
                else{
                    paymentStatus.text = "Not Received"
                }

//                It's used for change the color
                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )

                paymentStatus.setTextColor(colorMap[moneyStatus[position]]?: Color.BLACK)
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
            }
        }
    }
}