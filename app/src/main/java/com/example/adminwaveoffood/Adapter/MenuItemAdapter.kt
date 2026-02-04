package com.example.adminwaveoffood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.ItemItemBinding
import com.example.adminwaveoffood.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position: Int) -> Unit
): RecyclerView.Adapter<MenuItemAdapter.ViewHolder>(){
    private val itemQuantities = IntArray(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(position)
    }

    override fun getItemCount(): Int{
        return menuList.size
    }

    inner class ViewHolder(private val binding: ItemItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply{
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                foodNameId.text = menuItem.foodName
                foodPriceId.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)

                binding.imageButtonPlus.setOnClickListener{
                    increaseQuantity(position)
                }

                binding.imageButtonMinus.setOnClickListener {
                    decreaseQuantity(position)
                }

                binding.imageButtonDelete.setOnClickListener{
                    onDeleteClickListener(position)

//                    val itemPosition =  adapterPosition
//                    if(itemPosition != RecyclerView.NO_POSITION){
//                        deleteItem(position)
//                    }
                }
            }
        }

        private fun decreaseQuantity(position: Int){
            if(itemQuantities[position] > 1){
                itemQuantities[position]--
                binding.itemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.itemQuantity.text = itemQuantities[position].toString()
            }
        }

        fun deleteItem(position: Int){
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }
    }
}


