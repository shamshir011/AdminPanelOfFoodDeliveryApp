package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.PendingOrderItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val name: MutableList<String>,
    private val quantity: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked: OnItemClicked,
): RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>(){

    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder{
        val binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int){
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding): RecyclerView.ViewHolder(binding.root){

        private var isAccepted = false
        fun bind(position: Int){
            binding.apply {
                customerName.text = name[position]
                itemQuantity.text = quantity[position]
                var uriString = foodImage[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderFoodImage)

//                val uriString =  foodImage[position]
//                val uri = Uri.parse(uriString)
//                Glide.with(context).load(uri).into(orderFoodImage)


                orderAcceptButton.apply{
                    if(!isAccepted){
                        text = "Accept"
                    }
                    else{
                        text = "Dispatch"
                    }
                    setOnClickListener{
                        if(!isAccepted){
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        }
                        else{
                            name.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener{
                    itemClicked.onItemClickListener(position)
                }
            }
        }
        fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}