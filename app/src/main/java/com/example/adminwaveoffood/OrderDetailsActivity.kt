package com.example.adminwaveoffood

import android.R.attr.order
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderDetailsAdapter
import com.example.adminwaveoffood.Adapter.PendingOrderAdapter
import com.example.adminwaveoffood.databinding.ActivityOrderDetailsBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy{
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var orderDetails: OrderDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.acceptButton.setOnClickListener {

            val order = orderDetails ?: return@setOnClickListener
            val restaurantId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val userId = order.userUid ?: return@setOnClickListener
            val pushKey = order.itemPushKey ?: return@setOnClickListener

            val database = FirebaseDatabase.getInstance().reference

            val updates = hashMapOf<String, Any>(
                "restaurantOrders/$restaurantId/$pushKey/orderAccepted" to true,
                "restaurantOrders/$restaurantId/$pushKey/status" to "Accepted",

                "userOrders/$userId/$pushKey/orderAccepted" to true,
                "userOrders/$userId/$pushKey/status" to "Accepted",

                "OrderDetails/$pushKey/orderAccepted" to true,
                "OrderDetails/$pushKey/status" to "Accepted"
            )

            database.updateChildren(updates)
                .addOnSuccessListener {

//                    sendNotificationToUser(order)

                    Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, OrderAcceptedActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to accept order", Toast.LENGTH_SHORT).show()
                }
        }

        binding.orderReject.setOnClickListener {

            val order = orderDetails ?: return@setOnClickListener
            val restaurantId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val userId = order.userUid ?: return@setOnClickListener
            val pushKey = order.itemPushKey ?: return@setOnClickListener

            val database = FirebaseDatabase.getInstance().reference

            val updates = hashMapOf<String, Any>(
                "restaurantOrders/$restaurantId/$pushKey/orderAccepted" to false,
                "restaurantOrders/$restaurantId/$pushKey/status" to "Rejected",

                "userOrders/$userId/$pushKey/orderAccepted" to false,
                "userOrders/$userId/$pushKey/status" to "Rejected",

                "OrderDetails/$pushKey/orderAccepted" to false,
                "OrderDetails/$pushKey/status" to "Rejected"
            )

            database.updateChildren(updates)
                .addOnSuccessListener {

//                    sendRejectNotificationToUser(order)

                    Toast.makeText(this, "Order Rejected", Toast.LENGTH_SHORT).show()

                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to reject order", Toast.LENGTH_SHORT).show()
                }
        }

        // Get order from intent
        orderDetails = intent.getSerializableExtra("order") as OrderDetails?

        orderDetails?.let {

            // Set RecyclerView
            binding.orderDetailRecyclerView.layoutManager = LinearLayoutManager(this)

            val adapter = OrderDetailsAdapter(
                this,
                ArrayList(it.foodNames ?: emptyList()),
                ArrayList(it.foodImages ?: emptyList()),
                ArrayList(it.foodQuantities ?: emptyList()),
                ArrayList(it.foodPrices ?: emptyList())
            )

            binding.orderDetailRecyclerView.adapter = adapter
            binding.textViewAddress.text = it.address
            binding.textViewTotalAmount.text = it.totalPrice
        }
    }

//    private fun sendNotificationToUser(order: OrderDetails?) {
//
//        val userId = order?.userUid ?: return
//        FirebaseDatabase.getInstance().reference
//            .child("userNotifications")
//            .child(userId)
//            .push()
//            .setValue("Your order has been accepted by the restaurant")
//    }

//    private fun sendRejectNotificationToUser(order: OrderDetails?) {
//
//        val userId = order?.userUid ?: return
//        FirebaseDatabase.getInstance().reference
//            .child("userNotifications")
//            .child(userId)
//            .push()
//            .setValue("Sorry, your order has been rejected by the restaurant")
//    }
}