package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderAdapter
import com.example.adminwaveoffood.Adapter.PendingOrderAdapter
import com.example.adminwaveoffood.databinding.ActivityPendingOrderBinding
import com.example.adminwaveoffood.databinding.FragmentOrderBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(){
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: OrderAdapter
    private var orderList: ArrayList<OrderDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        adapter = OrderAdapter(this, orderList)
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerView.adapter = adapter

        loadOrders()
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadOrders() {
        binding.pendingProgressBar.visibility = View.VISIBLE
        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("restaurantOrders")
            .child(restaurantId)   // VERY IMPORTANT
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    orderList.clear()

                    for (orderSnapshot in snapshot.children) {

                        val order = orderSnapshot
                            .getValue(OrderDetails::class.java)

                        order?.itemPushKey = orderSnapshot.key

                        if (order != null) {
                            orderList.add(order)
                        }
                    }

                    binding.pendingProgressBar.visibility = View.GONE

                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}