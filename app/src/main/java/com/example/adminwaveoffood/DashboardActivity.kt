package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwaveoffood.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.cardViewManageCategories.setOnClickListener {
            val intent = Intent(this, ManageCategoriesActivity::class.java)
            startActivity(intent)
        }
    }
}