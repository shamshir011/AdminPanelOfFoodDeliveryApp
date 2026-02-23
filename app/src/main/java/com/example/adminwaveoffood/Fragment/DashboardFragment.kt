package com.example.adminwaveoffood.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adminwaveoffood.AddBannerImageActivity
import com.example.adminwaveoffood.LoginActivity
import com.example.adminwaveoffood.ManageCategoriesActivity
import com.example.adminwaveoffood.ManageFoodItemActivity
import com.example.adminwaveoffood.ManageRestaurantActivity
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment(){

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        binding.cardViewManageBanners.setOnClickListener {
            val intent = Intent(context, AddBannerImageActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewManageCategories.setOnClickListener {
            val intent = Intent(context, ManageCategoriesActivity::class.java)
            startActivity(intent)
        }

        binding.manageRestaurantId.setOnClickListener {
            val intent = Intent(context, ManageRestaurantActivity::class.java)
            startActivity(intent)
        }
        binding.manageFoodItemId.setOnClickListener {
            val intent = Intent(context, ManageFoodItemActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root

    }

}