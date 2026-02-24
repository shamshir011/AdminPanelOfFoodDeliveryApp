package com.example.adminwaveoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.Adapter.OrderDetailsAdapter
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.FragmentDashboardBinding
import com.example.adminwaveoffood.databinding.FragmentOrderBinding
import com.example.adminwaveoffood.model.OrderDetails

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private  var foodNames: ArrayList<String> = arrayListOf()
    private  var foodImages: ArrayList<String> = arrayListOf()
    private  var foodQuantity: ArrayList<Int> = arrayListOf()
    private  var foodPrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)

        getDataFromIntent()


        return binding.root
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->

            userName = receivedOrderDetails.userName
            foodNames = receivedOrderDetails.foodNames as ArrayList<String>
            foodImages = receivedOrderDetails.foodImages as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            foodPrices = receivedOrderDetails.foodPrices as ArrayList<String>
            totalPrice = receivedOrderDetails.totalPrice

            setAdapter()
        }
    }

    private fun setAdapter() {
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = OrderDetailsAdapter(requireContext(), foodNames,  foodQuantity, foodPrices)
        binding.orderRecyclerView.adapter = adapter
    }

}