package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffood.OrderDetailsActivity
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.HistoryItemDesignBinding
import com.example.adminwaveoffood.model.OrderDetails

class OrderHistoryAdapter(
    private val context: Context,
    private val orderList: List<OrderDetails>
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {

        val binding = HistoryItemDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    inner class OrderViewHolder(private val binding: HistoryItemDesignBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderDetails) {

            binding.apply {

                // Order ID
                textViewOrderId.text = "#${order.itemPushKey?.takeLast(6) ?: "N/A"}"

                // Food names
                textViewFoodName.text = order.foodNames?.joinToString(", ") ?: "N/A"

                // Total price
                textViewTotalPrice.text = "₹${order.totalPrice ?: "0"}"

                // Status
                textViewStatus.text = order.status ?: "Pending"

                // Change color according to status
                when(order.status){
                    "Delivered" -> textViewStatus.setTextColor(Color.parseColor("#2E7D32")) // Green
                    "Rejected" -> textViewStatus.setTextColor(
                        ContextCompat.getColor(context, R.color.crimsonRed)
                    )

                    "Accepted" -> textViewStatus.setTextColor(
                        ContextCompat.getColor(context, R.color.darkGreen)
                    )


                    "Pending" -> textViewStatus.setTextColor(Color.parseColor("#FFA000")) // Orange
                    else -> textViewStatus.setTextColor(Color.BLACK)
                }

                // Click → open OrderDetailsActivity
                root.setOnClickListener {
                    val intent = Intent(context, OrderDetailsActivity::class.java)
                    intent.putExtra("order", order)
                    context.startActivity(intent)

                }
            }
        }
    }
}