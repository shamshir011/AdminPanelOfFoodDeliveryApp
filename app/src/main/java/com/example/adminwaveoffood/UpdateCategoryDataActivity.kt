package com.example.adminwaveoffood
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.ActivityUpdateCategoryDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UpdateCategoryDataActivity : AppCompatActivity() {

    private var foodImageUri: Uri? = null
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("FoodCategory")

    private lateinit var categoryId: String
    private var oldImageUrl: String = ""
    private var newImageUrl: String = ""

    private val binding: ActivityUpdateCategoryDataBinding by lazy {
        ActivityUpdateCategoryDataBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        getAndSetData()



        binding.textViewChangeImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backNavigation.setOnClickListener{
            finish()
        }

        binding.saveChanges.setOnClickListener {
            val categoryName = binding.categoryNameUpdate.text.toString().trim()

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalImageUrl = newImageUrl.ifEmpty {
                    oldImageUrl
                }

            updateCategoryData(categoryId, categoryName, finalImageUrl)
        }
    }

    fun getAndSetData() {

        categoryId = intent.getStringExtra("categoryId") ?: ""

        if (categoryId.isEmpty()) {
            Toast.makeText(this, "Category ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val name = intent.getStringExtra("name")
        oldImageUrl = intent.getStringExtra("imageUrl") ?: ""

        binding.categoryNameUpdate.setText(name)

        Glide.with(this)
            .load(oldImageUrl)
            .into(binding.categoryImageUpdate)
    }



    private fun updateCategoryData(
        categoryId: String,
        categoryName: String,
        categoryImageUrl: String
    ) {
        val categoryRef = database.getReference("FoodCategory").child(categoryId)

        val updateData = hashMapOf<String, Any>(
            "foodCategoryName" to categoryName,
            "foodCategoryImage" to categoryImageUrl
        )

        categoryRef.updateChildren(updateData)
            .addOnSuccessListener {
                Toast.makeText(this, "Category updated successfully 😊", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Category update failed 😢", Toast.LENGTH_SHORT).show()
            }
    }





    private fun uploadImageAndGetUrl(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("category_images/${System.currentTimeMillis()}")

        storageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                storageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                newImageUrl = downloadUri.toString()
            }
    }
    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if(uri != null){
            binding.categoryImageUpdate.setImageURI(uri)
            foodImageUri = uri
            uploadImageAndGetUrl(uri)
        }
    }

}