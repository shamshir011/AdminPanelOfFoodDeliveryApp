package com.example.adminwaveoffood

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderAdapter
import com.example.adminwaveoffood.databinding.ActivityOrderAcceptedBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderAcceptedActivity : AppCompatActivity() {
    private val binding: ActivityOrderAcceptedBinding by lazy {
        ActivityOrderAcceptedBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: OrderAdapter
    private val acceptedOrderList = ArrayList<OrderDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.orderAcceptedRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(this, acceptedOrderList)
        binding.orderAcceptedRecyclerView.adapter = adapter
        loadAcceptedOrders()
    }

    private fun loadAcceptedOrders() {

        binding.orderAcceptedProgressBar.visibility = View.VISIBLE

        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("restaurantOrders")
            .child(restaurantId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    acceptedOrderList.clear()

                    for (orderSnapshot in snapshot.children) {

                        val order = orderSnapshot.getValue(OrderDetails::class.java)

                        if (order != null) {

                            order.itemPushKey = orderSnapshot.key

                            if (order.status == "Accepted") {

                                acceptedOrderList.add(order)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                    binding.orderAcceptedProgressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}