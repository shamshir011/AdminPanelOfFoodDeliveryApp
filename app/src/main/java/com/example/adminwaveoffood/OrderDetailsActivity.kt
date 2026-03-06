package com.example.adminwaveoffood

import android.R.attr.order
import android.content.Intent
import android.os.Bundle
import android.view.View
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

        binding.backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
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
                    Toast.makeText(this, "Order Rejected", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to reject order", Toast.LENGTH_SHORT).show()
                }
        }

//        Its for when order delivered
        binding.markDelivered.setOnClickListener {

            val order = orderDetails ?: return@setOnClickListener
            val restaurantId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val userId = order.userUid ?: return@setOnClickListener
            val pushKey = order.itemPushKey ?: return@setOnClickListener

            val database = FirebaseDatabase.getInstance().reference

            val updates = hashMapOf<String, Any>(
                "restaurantOrders/$restaurantId/$pushKey/status" to "Delivered",
                "userOrders/$userId/$pushKey/status" to "Delivered",
                "OrderDetails/$pushKey/status" to "Delivered"
            )

            database.updateChildren(updates)
                .addOnSuccessListener {

                    Toast.makeText(this, "Order Delivered", Toast.LENGTH_SHORT).show()

                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                }
        }

        // Get order from intent
        orderDetails = intent.getSerializableExtra("order") as OrderDetails?

        orderDetails?.let { order ->

            when(order.status){

                "Pending" -> {
                    binding.acceptButton.visibility = View.VISIBLE
                    binding.orderReject.visibility = View.VISIBLE
                    binding.markDelivered.visibility = View.GONE
                }

                "Accepted" -> {
                    binding.acceptButton.visibility = View.GONE
                    binding.orderReject.visibility = View.GONE
                    binding.markDelivered.visibility = View.VISIBLE
                }

                "Delivered", "Rejected" -> {
                    binding.acceptButton.visibility = View.GONE
                    binding.orderReject.visibility = View.GONE
                    binding.markDelivered.visibility = View.GONE
                }
            }
        }
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
}