package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityAddNewItemBinding
import com.example.adminwaveoffood.databinding.ActivityManageFoodItemBinding

class ManageFoodItemActivity : AppCompatActivity() {
    private val binding: ActivityManageFoodItemBinding by lazy {
        ActivityManageFoodItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addNewFoodItemId.setOnClickListener {
            val intent = Intent(this, AddNewItemActivity::class.java)
            startActivity(intent)
        }
    }
}