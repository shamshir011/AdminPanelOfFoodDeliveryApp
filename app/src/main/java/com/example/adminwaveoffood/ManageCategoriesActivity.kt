package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.MenuItemAdapter
import com.example.adminwaveoffood.Adapter.ShowCategoryAdapter
import com.example.adminwaveoffood.databinding.ActivityManageCategoriesBinding
import com.example.adminwaveoffood.model.AllMenu
import com.example.adminwaveoffood.model.FoodCategory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageCategoriesActivity : AppCompatActivity() {

    private lateinit var  databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var categoryItems: ArrayList<FoodCategory> = ArrayList()
    private val binding: ActivityManageCategoriesBinding by lazy {
        ActivityManageCategoriesBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveCategoryItems()

        binding.addNewCategoryId.setOnClickListener{
            val intent = Intent(this, AddNewCategoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun retrieveCategoryItems(){
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("FoodCategory")

        // Fetch data from data base
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear existing data before populating
                categoryItems.clear()

                // loop for through each food item
                for(foodSnapshot in snapshot.children){
                    val categoryItem = foodSnapshot.getValue(FoodCategory::class.java)
                    categoryItem?.let {
                        categoryItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error: ${error.message}")
            }
        })
    }


    private fun setAdapter() {
        val adapter = ShowCategoryAdapter(this, categoryItems, databaseReference){ position ->
            deleteMenuItems(position)
        }
        binding.categoryRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.categoryRecyclerview.adapter = adapter
    }

    private fun deleteMenuItems(position: Int){
        val categoryItemToDelete = categoryItems[position]
        val menuItemKey = categoryItemToDelete.key
        val foodMenuReference = database.reference.child("FoodCategory").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task->
            if(task.isSuccessful){
                categoryItems.removeAt(position)
                binding.categoryRecyclerview.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this,"Item not delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}