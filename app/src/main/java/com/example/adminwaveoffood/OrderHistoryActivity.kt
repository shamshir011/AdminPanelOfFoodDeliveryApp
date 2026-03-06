package com.example.adminwaveoffood

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderAdapter
import com.example.adminwaveoffood.Adapter.OrderHistoryAdapter
import com.example.adminwaveoffood.databinding.ActivityOrderHistoryBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHistoryActivity : AppCompatActivity() {
    private val binding: ActivityOrderHistoryBinding by lazy {
        ActivityOrderHistoryBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: OrderHistoryAdapter
    private val historyOrderList = ArrayList<OrderDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OrderHistoryAdapter(this, historyOrderList)
        binding.recyclerView.adapter = adapter
        loadHistoryOrders()

        binding.imageViewBackNavigation.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun loadHistoryOrders() {

        binding.orderHistoryProgressBar.visibility = View.VISIBLE

        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("restaurantOrders")
            .child(restaurantId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    historyOrderList.clear()

                    for (orderSnapshot in snapshot.children) {

                        val order = orderSnapshot.getValue(OrderDetails::class.java)

                        if (order != null) {

                            order.itemPushKey = orderSnapshot.key

                            // ✅ Show only completed orders in history
                            if (order.status == "Delivered" ||
                                order.status == "Rejected" ||
                                order.status == "Accepted") {
                                historyOrderList.add(order)
                            }
                        }
                    }

                    // Show latest orders first
                    historyOrderList.reverse()

                    adapter.notifyDataSetChanged()
                    binding.orderHistoryProgressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}