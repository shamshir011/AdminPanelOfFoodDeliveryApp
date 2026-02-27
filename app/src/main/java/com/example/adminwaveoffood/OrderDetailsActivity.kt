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

            val restaurantId = order.restaurantId
                ?: FirebaseAuth.getInstance().currentUser!!.uid

            val pushKey = order.itemPushKey ?: return@setOnClickListener

            FirebaseDatabase.getInstance().reference
                .child("restaurantOrders")
                .child(restaurantId)
                .child(pushKey)
                .updateChildren(
                    mapOf(
                        "orderAccepted" to true,
                        "status" to "Accepted"
                    )
                )
                .addOnSuccessListener {

                    sendNotificationToUser(order)

                    Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, OrderAcceptedActivity::class.java))
                    finish()
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

            // Optional: show user info
//            binding.o = it.userName
//            binding.textPhone.text = it.phoneNumber
            binding.textViewAddress.text = it.address
            binding.textViewTotalAmount.text = it.totalPrice
        }
    }

    private fun sendNotificationToUser(order: OrderDetails?) {

        val userId = order?.userUid ?: return

        FirebaseDatabase.getInstance().reference
            .child("userNotifications")
            .child(userId)
            .push()
            .setValue("Your order has been accepted by the restaurant")
    }
}