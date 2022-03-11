package com.example.domo.views.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.domo.viewModels.LogInViewModel

import com.example.domo.viewModels.ViewModelFactory
import com.example.featureLogIn.databinding.ActivityAuthorizationBinding
import extentions.appComponent

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private val viewModel: LogInViewModel by viewModels { ViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        );
//        setContentView(binding.root)
//        binding.lifecycleOwner = this
//        binding.viewModel = viewModel
    }
}