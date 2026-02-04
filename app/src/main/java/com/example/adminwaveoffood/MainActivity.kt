package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.adminwaveoffood.databinding.ActivityMainBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity(){

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)


        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val bottomNav: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)



//        binding.addMenu.setOnClickListener {
//            val intent = Intent(this, DashboardActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.cardViewAllItemMenu.setOnClickListener {
//            val intent = Intent(this, AllItemActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.orderDispatcherCardView.setOnClickListener {
//            val intent = Intent(this, OutForDeliveryActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.profileCardView.setOnClickListener {
//            val intent = Intent(this, AdminProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.createNewUser.setOnClickListener{
//            val intent = Intent(this, CreateUserActivity::class.java)
//            startActivity(intent)
//        }

//        binding.pendingOrderTextView.setOnClickListener{
//            val intent = Intent(this, PendingOrderActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.logoutButton.setOnClickListener{
//            auth.signOut()
//            startActivity(Intent(this, SignUpActivity::class.java))
//            finish()
//        }
//
//        binding.addBannerImage.setOnClickListener {
//            val intent = Intent(this, AddBannerImageActivity::class.java)
//            startActivity(intent)
//        }
//
//        pendingOrders()
//
//        completedOrders()
//
//        wholeTimeEarning()
//    }

//    private fun wholeTimeEarning(){
//        val listOfTotalPay = mutableListOf<Int>()
//        completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
//        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot){
//
//                for(orderSnapshot in snapshot.children){
//                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
//
//                    completeOrder?.totalPrice?.replace("₹","")?.toIntOrNull()
//                        ?.let{ i ->
//                            listOfTotalPay.add(i)
//                        }
//                }
//
//                binding.wholeTimeEarning.text = listOfTotalPay.sum().toString() + "₹"
//            }
//
//            override fun onCancelled(error: DatabaseError){
//
//            }
//        })
//    }

//    private fun completedOrders(){
//        val completedOrderReference = database.reference.child("CompletedOrder")
//        var completedOrderItemCount = 0
//
//        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot){
//                completedOrderItemCount = snapshot.childrenCount.toInt()
//                binding.completeOrders.text = completedOrderItemCount.toString()
//            }
//
//            override fun onCancelled(error: DatabaseError){
//
//            }
//        })
//    }
//
//    // Counting total items
//    private fun pendingOrders() {
//        database = FirebaseDatabase.getInstance()
//        val pendingOrderReference = database.reference.child("OrderDetails")
//        var pendingOrderItemCount = 0
//
//        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot){
//                pendingOrderItemCount = snapshot.childrenCount.toInt()
//                binding.pendingOrders.text = pendingOrderItemCount.toString()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
    }
}