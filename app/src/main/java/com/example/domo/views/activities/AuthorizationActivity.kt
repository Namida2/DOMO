package com.example.domo.views.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.domo.viewModels.LogInViewModel
import com.example.domo.viewModels.ViewModelFactory
import com.example.featureLogIn.databinding.ActivityAuthorizationBinding

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private val viewModel: LogInViewModel by viewModels { ViewModelFactory }

}