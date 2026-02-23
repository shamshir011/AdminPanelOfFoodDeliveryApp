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
import com.example.adminwaveoffood.databinding.ActivityManageRestaurantBinding
import com.example.adminwaveoffood.model.FoodCategory
import com.example.adminwaveoffood.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class ManageRestaurantActivity : AppCompatActivity() {
    private val binding: ActivityManageRestaurantBinding by lazy {
        ActivityManageRestaurantBinding.inflate(layoutInflater)
    }

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
//            restaurantStatus = binding.switchStatus.isChecked

            if(!(restaurantName.isBlank() || restaurantPhone.isBlank() || restaurantAddress.isBlank()
                        || restaurantDeliveryRadius.isBlank() || restaurantDeliveryDuration.isBlank() || restaurantOpeningTime.isBlank()  || restaurantClosingTime.isBlank())){
                uploadData()
                binding.restaurantImage.setImageDrawable(null)
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


//        binding.switchStatus.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                binding.statusLeftOpen.text = "Open"
//                binding.statusRightOpen.text = "Open"
//                binding.statusLeftOpen.setTextColor(getColor(R.color.darkGreen))
//                binding.statusRightOpen.setTextColor(getColor(R.color.darkGreen))
//            } else {
//                binding.statusLeftOpen.text = "Closed"
//                binding.statusRightOpen.text = "Closed"
//                binding.statusLeftOpen.setTextColor(getColor(R.color.red))
//                binding.statusRightOpen.setTextColor(getColor(R.color.red))
//            }
//        }
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


//    private fun uploadData(){
//        // get a reference to the "menu" node in the database
//        val menuRef = database.getReference("Restaurant")
//        //Generate a unique key for the new menu item
//        val newItemKey = menuRef.push().key
//
//        if(foodImageUri != null){
//            val storageRef = FirebaseStorage.getInstance().reference
//            val imageRef = storageRef.child("restaurant_images/${newItemKey}.jpg")
//            val uploadTask = imageRef.putFile(foodImageUri!!)
//
////            val restaurantStatus = binding.switchStatus.isChecked
//
//            uploadTask.addOnSuccessListener{
//                imageRef.downloadUrl.addOnSuccessListener{
//                        downloadUrl->
//                    //Create a new menu item
//                    val newItem = Restaurant(
//                        newItemKey,
//                        restaurantImage = downloadUrl.toString(),
//                        restaurantName = restaurantName,
//                        restaurantPhone = restaurantPhone,
//                        restaurantAddress = restaurantAddress,
//                        restaurantDeliveryRadius = restaurantDeliveryRadius,
//                        restaurantDeliveryDuration = restaurantDeliveryDuration,
//                        restaurantOpeningTime = restaurantOpeningTime,
//                        restaurantClosingTime = restaurantClosingTime,
////                        restaurantStatus = restaurantStatus
//
//                    )
//                    newItemKey?.let {
//                            key->
//                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
//                            Toast.makeText(this, "data uploaded successfully", Toast.LENGTH_SHORT).show()
//                        }.addOnFailureListener {
//                            Toast.makeText(this, "data uploaded failed", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }.addOnFailureListener{
//                Toast.makeText(this, "image upload failed", Toast.LENGTH_SHORT).show()
//            }
//        }
////        else{
////            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
////        }
//    }


    private fun uploadData() {

        val categoryMap = hashMapOf<String, Boolean>()

        if (binding.cbPizza.isChecked) {
            categoryMap["Pizza"] = true
        }

        if (binding.cbItalian.isChecked) {
            categoryMap["Italian"] = true
        }

        if (binding.cbFastFood.isChecked) {
            categoryMap["FastFood"] = true
        }

        if (categoryMap.isEmpty()) {
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
                        restaurantCategories = categoryMap,
                        restaurantOpeningTime = restaurantOpeningTime,
                        restaurantClosingTime = restaurantClosingTime
                    )

                    menuRef.setValue(newItem)
                        .addOnSuccessListener {
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