package com.example.adminwaveoffood.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adminwaveoffood.AddBannerImageActivity
import com.example.adminwaveoffood.ManageCategoriesActivity
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(){

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)

        binding.cardViewManageBanners.setOnClickListener {
            val intent = Intent(context, AddBannerImageActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewManageCategories.setOnClickListener {
            val intent = Intent(context, ManageCategoriesActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

}