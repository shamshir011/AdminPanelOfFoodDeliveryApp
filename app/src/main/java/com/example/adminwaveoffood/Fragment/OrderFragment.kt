package com.example.adminwaveoffood.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderAdapter
import com.example.adminwaveoffood.ManageCategoriesActivity
import com.example.adminwaveoffood.OrderAcceptedActivity
import com.example.adminwaveoffood.PendingOrderActivity
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

        binding.pendingOrder.setOnClickListener {
            val intent = Intent(context, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        binding.acceptedOrder.setOnClickListener {
            val intent = Intent(context, OrderAcceptedActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

//    private fun loadOrders() {
//        binding.progressBar.visibility = View.VISIBLE
//        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid
//
//        FirebaseDatabase.getInstance().reference
//            .child("restaurantOrders")
//            .child(restaurantId)   // VERY IMPORTANT
//            .addValueEventListener(object : ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    orderList.clear()
//
//                    for (orderSnapshot in snapshot.children) {
//
//                        val order = orderSnapshot
//                            .getValue(OrderDetails::class.java)
//
//                        order?.itemPushKey = orderSnapshot.key
//
//                        if (order != null) {
//                            orderList.add(order)
//                        }
//                    }
//
//// ***********************                   Its for counting recyclerview item     ***************************************
//
//                    binding.textViewPendingCount.text = "${orderList.size}"
//                    adapter.notifyDataSetChanged()
//                    binding.progressBar.visibility = View.GONE
//
//                    adapter.notifyDataSetChanged()
//                }
//                override fun onCancelled(error: DatabaseError) {}
//            })
//    }


//    private fun loadOrders() {
//
//        binding.progressBar.visibility = View.VISIBLE
//
//        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid
//
//        FirebaseDatabase.getInstance().reference
//            .child("restaurantOrders")
//            .child(restaurantId)
//            .addValueEventListener(object : ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    orderList.clear()
//                    var pendingCount = 0
//
//                    for (orderSnapshot in snapshot.children){
//
//                        val order = orderSnapshot.getValue(OrderDetails::class.java)
//
//                        if (order != null) {
//
//                            order.itemPushKey = orderSnapshot.key
//
////                            if (order.status == "Pending") {
////
////                                orderList.add(order)
////                                pendingCount++
////                            }
//                            if (order.orderAccepted == false) {
//                                orderList.add(order)
//                                pendingCount++
//                            }
//                        }
//                    }
//
//                    binding.textViewPendingCount.text = pendingCount.toString()
//
//                    adapter.notifyDataSetChanged()
//                    binding.progressBar.visibility = View.GONE
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
//    }

    private fun loadOrders() {

        binding.progressBar.visibility = View.VISIBLE

        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("restaurantOrders")
            .child(restaurantId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    orderList.clear()

                    var pendingCount = 0
                    var acceptedCount = 0   // 👈 NEW

                    for (orderSnapshot in snapshot.children){

                        val order = orderSnapshot.getValue(OrderDetails::class.java)

                        if (order != null) {

                            order.itemPushKey = orderSnapshot.key

                            if (!order.orderAccepted) {
                                orderList.add(order)   // Show only pending in list
                                pendingCount++
                            } else {
                                acceptedCount++       // 👈 Count accepted
                            }
                        }
                    }

                    binding.textViewPendingCount.text = pendingCount.toString()
                    binding.textViewAcceptedOrder.text = acceptedCount.toString() // 👈 NEW

                    adapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}