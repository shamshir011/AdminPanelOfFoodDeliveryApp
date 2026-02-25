package com.example.adminwaveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderDetailsAdapter
import com.example.adminwaveoffood.Adapter.PendingOrderAdapter
import com.example.adminwaveoffood.databinding.ActivityOrderDetailsBinding
import com.example.adminwaveoffood.model.OrderDetails

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
}