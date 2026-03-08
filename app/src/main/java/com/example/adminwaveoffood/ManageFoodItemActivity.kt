package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.ShowFoodItemAdapter
import com.example.adminwaveoffood.databinding.ActivityAddNewItemBinding
import com.example.adminwaveoffood.databinding.ActivityManageFoodItemBinding
import com.example.adminwaveoffood.model.AddItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageFoodItemActivity : AppCompatActivity() {
    private val binding: ActivityManageFoodItemBinding by lazy {
        ActivityManageFoodItemBinding.inflate(layoutInflater)
    }
    private lateinit var foodList: ArrayList<AddItem>
    private lateinit var adapter: ShowFoodItemAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addNewFoodItemId.setOnClickListener{
            val intent = Intent(this, AddNewItemActivity::class.java)
            startActivity(intent)
        }


        foodList = ArrayList()

        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid

        databaseReference = FirebaseDatabase.getInstance()
            .getReference("foodItem")
            .child(restaurantId)

        adapter = ShowFoodItemAdapter(this, foodList) { position ->
            deleteItem(position)
        }

        binding.addFoodItemRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.addFoodItemRecyclerview.adapter = adapter

        loadFoodItems()
    }

    private fun loadFoodItems() {

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                foodList.clear()

                for (foodSnapshot in snapshot.children) {

                    val item = foodSnapshot.getValue(AddItem::class.java)

                    if (item != null) {
                        foodList.add(item)
                    }
                }

                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun deleteItem(position: Int) {

        val item = foodList[position]

        val key = item.key ?: return

        databaseReference.child(key).removeValue()
    }
}