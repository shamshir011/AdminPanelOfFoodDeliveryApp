package com.example.adminwaveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.ActivityUpdateFoodItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UpdateFoodItemActivity : AppCompatActivity() {

    private var foodImageUri: Uri? = null

    private val database = FirebaseDatabase.getInstance()
    private val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid

    private lateinit var itemKey: String

    private var oldImageUrl: String = ""
    private var newImageUrl: String = ""

    private val binding: ActivityUpdateFoodItemBinding by lazy {
        ActivityUpdateFoodItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        getAndSetData()

        binding.textViewChangeImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backNavigation.setOnClickListener {
            finish()
        }

        binding.saveChanges.setOnClickListener {

            val title = binding.editTextName.text.toString().trim()
            val price = binding.editTextPrice.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()

            if (title.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Title and Price required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalImageUrl = if (newImageUrl.isEmpty()) oldImageUrl else newImageUrl

            updateFoodItem(itemKey, title, price, description, finalImageUrl)
        }
    }

    private fun getAndSetData() {

        itemKey = intent.getStringExtra("itemKey") ?: ""

        if (itemKey.isEmpty()) {
            Toast.makeText(this, "Item ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val title = intent.getStringExtra("title")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")

        oldImageUrl = intent.getStringExtra("imageUrl") ?: ""

        binding.editTextName.setText(title)
        binding.editTextPrice.setText(price)
        binding.editTextDescription.setText(description)

        Glide.with(this)
            .load(oldImageUrl)
            .into(binding.imageViewUpdate)
    }

    private fun updateFoodItem(
        itemKey: String,
        title: String,
        price: String,
        description: String,
        imageUrl: String
    ) {

        val itemRef = database
            .getReference("foodItem")
            .child(restaurantId)
            .child(itemKey)

        val updateData = hashMapOf<String, Any>(
            "title" to title,
            "price" to price,
            "description" to description,
            "itemImage" to imageUrl
        )

        itemRef.updateChildren(updateData)
            .addOnSuccessListener {
                Toast.makeText(this, "Food item updated successfully 😊", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update failed 😢", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageAndGetUrl(uri: Uri) {

        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("item_images/${System.currentTimeMillis()}")

        storageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                storageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                newImageUrl = downloadUri.toString()
            }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            if (uri != null) {
                binding.imageViewUpdate.setImageURI(uri)
                foodImageUri = uri
                uploadImageAndGetUrl(uri)
            }
        }
}