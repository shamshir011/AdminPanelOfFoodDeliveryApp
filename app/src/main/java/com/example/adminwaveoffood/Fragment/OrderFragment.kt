package com.example.adminwaveoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderAdapter
import com.example.adminwaveoffood.Adapter.OrderDetailsAdapter
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.FragmentDashboardBinding
import com.example.adminwaveoffood.databinding.FragmentOrderBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapter: OrderAdapter
    private var orderList: ArrayList<OrderDetails> = ArrayList()

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOrderBinding.inflate(inflater, container, false)

        // Initialize adapter
        adapter = OrderAdapter(requireContext(), orderList)
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRecyclerView.adapter = adapter

        loadOrders()

        return binding.root
    }

    private fun loadOrders() {

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

                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}