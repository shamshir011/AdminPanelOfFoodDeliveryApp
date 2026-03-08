package com.example.adminwaveoffood.Fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.*
import android.graphics.Color
import com.example.adminwaveoffood.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class AnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)


        countTotalOrders()
        totalEarning()
        rejectedOrders()
        pendingOrders()
        analyticsGraph()

        return binding.root
    }
    private fun countTotalOrders(){
        val database = FirebaseDatabase.getInstance().reference.child("OrderDetails")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val totalOrders = snapshot.childrenCount

                binding.textViewTotalOrder.text = totalOrders.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun totalEarning(){
        val database = FirebaseDatabase.getInstance().reference.child("OrderDetails")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalEarnings = 0
                for(orderSnapshot in snapshot.children){
                    val status = orderSnapshot.child("status").value.toString()
                    if(status == "Delivered"){   // Only count completed orders
                        val price = orderSnapshot.child("totalPrice").value.toString().toIntOrNull() ?: 0
                        totalEarnings += price
                    }
                }
                binding.textViewTotalEarn.text = "₹$totalEarnings"
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun rejectedOrders(){
        val database = FirebaseDatabase.getInstance().reference.child("OrderDetails")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var rejectedOrders = 0
                for(orderSnapshot in snapshot.children){
                    val status = orderSnapshot.child("status").value.toString()
                    if(status == "Rejected"){
                        rejectedOrders++
                    }
                }
                binding.textViewRejectOrder.text = rejectedOrders.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun pendingOrders(){
        val database = FirebaseDatabase.getInstance().reference.child("OrderDetails")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var pendingOrders = 0
                for(orderSnapshot in snapshot.children){
                    val status = orderSnapshot.child("status").value.toString()
                    if(status == "Accepted"){
                        pendingOrders++
                    }
                }
                binding.textViewPendingOrder.text = pendingOrders.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun analyticsGraph() {
        val database = FirebaseDatabase.getInstance().reference.child("OrderDetails")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempEntries = ArrayList<Entry>()
                // Collect delivered orders
                for (orderSnapshot in snapshot.children) {
                    val status = orderSnapshot.child("status").value?.toString() ?: continue
                    if (status != "Delivered") continue
                    val price = orderSnapshot.child("totalPrice")
                        .value?.toString()?.toFloatOrNull() ?: 0f
                    tempEntries.add(Entry(0f, price))
                }
                if (tempEntries.isEmpty()) {
                    binding.salesChart.setNoDataText("No delivered orders yet")
                    binding.salesChart.invalidate()
                    return
                }
                // Reverse so oldest orders appear first
                tempEntries.reverse()
                // Create entries with proper x-values
                val entries = ArrayList<Entry>()
                for (i in tempEntries.indices) {
                    entries.add(Entry(i.toFloat(), tempEntries[i].y))
                }
                val dataSet = LineDataSet(entries, "Sales").apply {
                    color = Color.BLUE
                    lineWidth = 3f
                    setCircleColor(Color.BLUE)
                    circleRadius = 5f
                    setDrawValues(true)
                    valueTextSize = 10f
                    valueTextColor = Color.BLACK

                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawFilled(true)
                    fillAlpha = 80
                    fillColor = Color.BLUE
                }
                val chart = binding.salesChart
                chart.data = LineData(dataSet)

                chart.description.isEnabled = false
                chart.setTouchEnabled(true)
                chart.setPinchZoom(true)

                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false

                val xAxis = chart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)

                val leftAxis = chart.axisLeft
                leftAxis.textColor = Color.DKGRAY
                leftAxis.setDrawGridLines(true)

                chart.invalidate()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

}