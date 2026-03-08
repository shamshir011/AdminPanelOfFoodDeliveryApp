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

//        val chart = binding.salesChart
//        val entries = ArrayList<Entry>()
//        entries.add(Entry(1f, 500f))
//        entries.add(Entry(2f, 700f))
//        entries.add(Entry(3f, 650f))
//        entries.add(Entry(4f, 900f))
//        entries.add(Entry(5f, 1000f))
//
//        val dataSet = LineDataSet(entries, "Sales")
//
//        dataSet.color = Color.BLUE
//        dataSet.valueTextColor = Color.BLACK
//        dataSet.lineWidth = 3f
//
//        val lineData = LineData(dataSet)
//
//        chart.data = lineData
//        chart.invalidate()

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

        val days = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")

        val database = FirebaseDatabase.getInstance().reference.child("OrderDetails")

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val entries = ArrayList<Entry>()
                var index = 0f

                for(orderSnapshot in snapshot.children){

                    val status = orderSnapshot.child("status").value.toString()

                    if(status == "Delivered"){

                        val price = orderSnapshot.child("totalPrice")
                            .value.toString().toFloatOrNull() ?: 0f

                        entries.add(Entry(index, price))
                        index++
                    }
                }

                val dataSet = LineDataSet(entries, "Sales")

                dataSet.color = Color.BLUE
                dataSet.lineWidth = 3f
                dataSet.setCircleColor(Color.BLUE)
                dataSet.circleRadius = 5f
                dataSet.setDrawValues(true)
                dataSet.valueTextSize = 10f
                dataSet.valueTextColor = Color.BLACK

                dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                dataSet.setDrawFilled(true)
                dataSet.fillAlpha = 80
                dataSet.fillColor = Color.BLUE

                val lineData = LineData(dataSet)

                val chart = binding.salesChart
                chart.data = lineData

                chart.description.isEnabled = false
                chart.setTouchEnabled(true)
                chart.setPinchZoom(true)

                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false

                val xAxis = chart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(days)
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