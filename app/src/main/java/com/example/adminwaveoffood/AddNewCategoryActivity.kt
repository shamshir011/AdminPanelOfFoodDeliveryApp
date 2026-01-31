package com.example.adminwaveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwaveoffood.databinding.ActivityAddNewCategoryBinding
import com.example.adminwaveoffood.model.FoodCategory
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddNewCategoryActivity : AppCompatActivity() {
    private val binding: ActivityAddNewCategoryBinding by lazy {
        ActivityAddNewCategoryBinding.inflate(layoutInflater)
    }

    private lateinit var foodCategoryName: String
    private var foodImageUri: Uri? = null
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        binding.saveCategory.setOnClickListener {
            foodCategoryName = binding.foodCategoryName.text.toString().trim()
            if(!(foodCategoryName.isBlank())){
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun uploadData(){
        // get a reference to the "menu" node in the database
        val menuRef = database.getReference("FoodCategory")
        //Generate a unique key for the new menu item
        val newItemKey = menuRef.push().key

        if(foodImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("category_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener{
                imageRef.downloadUrl.addOnSuccessListener{
                        downloadUrl->
                    //Create a new menu item
                    val newItem = FoodCategory(
                        newItemKey,
                        foodCategoryName = foodCategoryName,
                        foodCategoryImage = downloadUrl.toString()
                    )
                    newItemKey?.let {
                            key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "data uploaded successfully", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "data uploaded failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(this, "image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if(uri != null){
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }


}