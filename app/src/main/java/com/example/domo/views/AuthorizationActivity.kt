package com.example.domo.views

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.domo.databinding.ActivityAuthorizationBinding
import com.example.domo.viewModels.LogInViewModel
import android.util.DisplayMetrics
import application.appComponent
import com.example.domo.viewModels.ViewModelFactory

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private val viewModel: LogInViewModel by viewModels { ViewModelFactory(appComponent) }

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}