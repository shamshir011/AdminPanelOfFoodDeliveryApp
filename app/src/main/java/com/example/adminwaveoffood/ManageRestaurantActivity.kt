package com.example.adminwaveoffood

import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffood.Adapter.CategoryAdapter
import com.example.adminwaveoffood.databinding.ActivityManageRestaurantBinding
import com.example.adminwaveoffood.model.FoodCategory
import com.example.adminwaveoffood.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class ManageRestaurantActivity : AppCompatActivity() {
    private val binding: ActivityManageRestaurantBinding by lazy {
        ActivityManageRestaurantBinding.inflate(layoutInflater)
    }
    val categoryNames = ArrayList<String>()
    val selectedCategories = HashMap<String, Boolean>()
    lateinit var adapter: CategoryAdapter

    private var foodImageUri: Uri? = null
    private lateinit var restaurantName: String
    private lateinit var restaurantPhone: String
    private lateinit var restaurantAddress: String
    private lateinit var restaurantDeliveryRadius: String
    private lateinit var restaurantDeliveryDuration: String
    private lateinit var restaurantOpeningTime: String
    private lateinit var restaurantClosingTime: String
//    private var restaurantStatus: Boolean = false
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()


        binding.backNavigation.setOnClickListener{
            finish()
        }

//        ***********       For opening time        ***************
        binding.openingTimeLayout.setOnClickListener{
            openTimePicker()
        }

        binding.openingTimeLayout.setEndIconOnClickListener {
            openTimePicker()
        }

        //        ***********       For closing time        ***************
        binding.closingTimeLayout.setOnClickListener{
            closeTimePicker()
        }

        binding.closingTimeLayout.setEndIconOnClickListener {
            closeTimePicker()
        }

        binding.uploadData.setOnClickListener {

            restaurantName = binding.etName.text.toString().trim()
            restaurantPhone = binding.etPhone.text.toString().trim()
            restaurantAddress = binding.etAddress.text.toString().trim()
            restaurantDeliveryRadius = binding.etDeliveryRadius.text.toString().trim()
            restaurantDeliveryDuration = binding.etDeliveryDuration.text.toString().trim()
            restaurantOpeningTime = binding.openingTime.text.toString().trim()
            restaurantClosingTime = binding.closingTime.text.toString().trim()

            if(!(restaurantName.isBlank() || restaurantPhone.isBlank() || restaurantAddress.isBlank()
                        || restaurantDeliveryRadius.isBlank() || restaurantDeliveryDuration.isBlank() || restaurantOpeningTime.isBlank()  || restaurantClosingTime.isBlank())){
                uploadData()
                binding.restaurantImage.setImageResource(R.drawable.add_category_item_icon)
                binding.etName.setText("")
                binding.etPhone.setText("")
                binding.etAddress.setText("")
                binding.etDeliveryRadius.setText("")
                binding.etDeliveryDuration.setText("")
                binding.openingTime.setText("")
                binding.closingTime.setText("")

            }
            else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewSelectImage.setOnClickListener{
            pickImage.launch("image/*")
        }



        adapter = CategoryAdapter(this, categoryNames, selectedCategories)
        binding.checkBoxCategoryRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL, false)
        binding.checkBoxCategoryRecyclerView.adapter = adapter
        loadCheckBoxCategory()
    }

    private fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime =
                    String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.openingTime.setText(formattedTime)
            },
            hour,
            minute,
            true
        ).show()
    }

    private fun closeTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime =
                    String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.closingTime.setText(formattedTime)
            },
            hour,
            minute,
            true
        ).show()
    }

    private fun loadCheckBoxCategory(){
        val ref = FirebaseDatabase.getInstance().reference.child("FoodCategory")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                categoryNames.clear()

                for (data in snapshot.children) {

                    val name = data.child("foodCategoryName")
                        .getValue(String::class.java)

                    if (name != null) {
                        categoryNames.add(name)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun uploadData() {

        if (selectedCategories.isEmpty()) {
            Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show()
            return
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        if (foodImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val restaurantId = currentUser.uid   // ✅ THIS IS THE KEY

        val menuRef = database
            .getReference("Restaurant")
            .child(restaurantId)   // ✅ store under UID

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("restaurant_images/$restaurantId.jpg")

        imageRef.putFile(foodImageUri!!)
            .addOnSuccessListener {

                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    val newItem = Restaurant(
                        key = restaurantId,   // ✅ key = UID
                        restaurantImage = downloadUrl.toString(),
                        restaurantName = restaurantName,
                        restaurantPhone = restaurantPhone,
                        restaurantAddress = restaurantAddress,
                        restaurantDeliveryRadius = restaurantDeliveryRadius,
                        restaurantDeliveryDuration = restaurantDeliveryDuration,
                        restaurantCategories = HashMap(selectedCategories),
                        restaurantOpeningTime = restaurantOpeningTime,
                        restaurantClosingTime = restaurantClosingTime
                    )

                    menuRef.setValue(newItem)
                        .addOnSuccessListener {

                            selectedCategories.clear()
                            adapter.notifyDataSetChanged()
                            foodImageUri = null

                            Toast.makeText(this, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Data upload failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }
    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if(uri != null){
            binding.restaurantImage.setImageURI(uri)
            foodImageUri = uri
        }
    }

}