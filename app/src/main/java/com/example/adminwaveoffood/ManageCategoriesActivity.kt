package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwaveoffood.databinding.ActivityManageCategoriesBinding

class ManageCategoriesActivity : AppCompatActivity() {
    private val binding: ActivityManageCategoriesBinding by lazy {
        ActivityManageCategoriesBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addNewCategoryId.setOnClickListener{
            val intent = Intent(this, AddNewCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}