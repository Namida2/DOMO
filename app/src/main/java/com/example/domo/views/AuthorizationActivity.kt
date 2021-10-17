package com.example.domo.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.domo.R
import com.example.domo.databinding.ActivityAuthorizationBinding
import com.example.domo.viewModels.LogInViewModel


class AuthorizationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private val viewModel: LogInViewModel by viewModels()

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}