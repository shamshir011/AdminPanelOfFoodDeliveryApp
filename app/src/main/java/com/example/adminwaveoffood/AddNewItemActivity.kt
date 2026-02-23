package com.example.adminwaveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityAddNewCategoryBinding
import com.example.adminwaveoffood.databinding.ActivityAddNewItemBinding
import com.example.adminwaveoffood.model.AddItem
import com.example.adminwaveoffood.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddNewItemActivity : AppCompatActivity() {

    private val binding: ActivityAddNewItemBinding by lazy {
        ActivityAddNewItemBinding.inflate(layoutInflater)
    }

    private var itemImageUri: Uri? = null
    private lateinit var title: String
    private lateinit var price: String
    private lateinit var description: String

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()



        binding.backNavigation.setOnClickListener{
            finish()
        }

        binding.textViewSelectItemImage.setOnClickListener{
            pickImage.launch("image/*")
        }

        binding.uploadData.setOnClickListener {

            title = binding.etTitle.text.toString().trim()
            price = binding.etPrice.text.toString().trim()
            description = binding.etDescription.text.toString().trim()
//            restaurantStatus = binding.switchStatus.isChecked

            if(!(title.isBlank() || price.isBlank() || description.isBlank())){
                uploadData()
            }
            else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun uploadData() {
//
//        val restaurantId = FirebaseAuth.getInstance().currentUser!!.uid
//
//        val menuRef = FirebaseDatabase.getInstance()
//            .getReference("foodItem")
//            .child(restaurantId)
//
//        val newItemKey = menuRef.push().key ?: return
//
//        if (itemImageUri != null) {
//
//            val storageRef = FirebaseStorage.getInstance().reference
//            val imageRef = storageRef.child("item_images/$restaurantId/$newItemKey.jpg")
//
//            imageRef.putFile(itemImageUri!!)
//                .addOnSuccessListener {
//
//                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//
//                        val newItem = AddItem(
//                            key = newItemKey,
//                            restaurantId = restaurantId,
//                            itemImage = downloadUrl.toString(),
//                            title = title,
//                            price = price,
//                            description = description
//                        )
//
//                        menuRef.child(newItemKey)
//                            .setValue(newItem)
//                            .addOnSuccessListener {
//                                Toast.makeText(this, "Item Uploaded", Toast.LENGTH_SHORT).show()
//                            }
//                    }
//                }
//        }
//    }


    private fun uploadData() {

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null){
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        if (itemImageUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show()
            return
        }

        val restaurantId = currentUser.uid

        val menuRef = FirebaseDatabase.getInstance()
            .getReference("foodItem")
            .child(restaurantId)

        val newItemKey = menuRef.push().key ?: return

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("item_images/$restaurantId/$newItemKey.jpg")

        imageRef.putFile(itemImageUri!!)
            .addOnSuccessListener {

                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    val newItem = AddItem(
                        key = newItemKey,
                        restaurantId = restaurantId,
                        itemImage = downloadUrl.toString(),
                        title = title,
                        price = price,
                        description = description
                    )

                    menuRef.child(newItemKey)
                        .setValue(newItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Item Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            // Clear UI
                            binding.addImage.setImageDrawable(null)
                            binding.etTitle.setText("")
                            binding.etPrice.setText("")
                            binding.etDescription.setText("")
                            itemImageUri = null
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Database Upload Failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }



    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if(uri != null){
            binding.addImage.setImageURI(uri)
            itemImageUri = uri
        }
    }
}