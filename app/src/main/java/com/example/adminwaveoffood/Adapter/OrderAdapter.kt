package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffood.OrderDetailsActivity
import com.example.adminwaveoffood.databinding.OrdersItemBinding
import com.example.adminwaveoffood.model.OrderDetails

class OrderAdapter(
    private val context: Context,
    private val orderList: List<OrderDetails>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: OrdersItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderDetails) {

            binding.apply {

                // Customer name
                textViewName.text = order.userName ?: "Unknown"

                // Phone number
                textViewPhoneNumber.text = order.phoneNumber ?: "N/A"

                // Short Order ID (last 6 characters)
                textView31.text = order.itemPushKey?.takeLast(6) ?: "N/A"

                // Click → open OrderDetailsActivity
                root.setOnClickListener {

                    val intent = Intent(context, OrderDetailsActivity::class.java)
                    intent.putExtra("order", order)
                    context.startActivity(intent)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {

        val binding = OrdersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        holder.bind(orderList[position])

    }
}