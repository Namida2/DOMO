package com.example.featureLogIn.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.core.domain.Employee
import com.example.core.domain.tools.extensions.logD
import com.example.featureLogIn.R
import com.example.featureLogIn.databinding.ActivityAuthorizationBinding
import com.example.featureLogIn.domain.interfaces.EmployeeAuthorizationCallback

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}