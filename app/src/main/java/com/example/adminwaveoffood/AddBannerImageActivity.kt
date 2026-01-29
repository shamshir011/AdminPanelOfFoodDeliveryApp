package com.example.adminwaveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityAddBannerImageBinding
import com.example.adminwaveoffood.model.AllMenu
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddBannerImageActivity : AppCompatActivity() {
    private val binding: ActivityAddBannerImageBinding by lazy {
        ActivityAddBannerImageBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var foodImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.selectBannerImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.selectedBannerImage.setImageURI(uri)
                foodImageUri = uri
            }
        }
    }

    private fun uploadData(){
        // get a reference to the "menu" node in the database
        val menuRef = database.getReference("bannerImage")
        //Generate a unique key for the new menu item
        val newItemKey = menuRef.push().key

        if(foodImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener{
                imageRef.downloadUrl.addOnSuccessListener{
                        downloadUrl->
                    //Create a new menu item
                    val newItem = AllMenu(
                        newItemKey,
                        foodImage = downloadUrl.toString()
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
            binding.selectedBannerImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}