package com.example.adminwaveoffood.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.adminwaveoffood.LoginActivity
import com.example.adminwaveoffood.R
import com.example.adminwaveoffood.databinding.FragmentDashboardBinding
import com.example.adminwaveoffood.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.cardViewLogout.setOnClickListener {

            val dialogMessage = AlertDialog.Builder(requireContext())
            dialogMessage.setTitle("Quiz Game")
            dialogMessage.setMessage("If you want sign out click Yes\notherwise click No.")
            dialogMessage.setCancelable(false)

            dialogMessage.setPositiveButton("Yes") { _, _ ->

                auth.signOut()

                val googleSignInClient = GoogleSignIn.getClient(
                    requireContext(),
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
                )

                googleSignInClient.signOut()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            dialogMessage.setNegativeButton("No") { _, _ ->
                Toast.makeText(requireContext(), "I don't want to Sign Out", Toast.LENGTH_SHORT).show()
            }

            dialogMessage.create().show()
        }

            return binding.root
    }
}