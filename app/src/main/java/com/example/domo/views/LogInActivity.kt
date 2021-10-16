package com.example.domo.views

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import application.appComponent
import com.example.domo.R
import com.example.domo.databinding.ActivityLogInBinding
import com.example.domo.viewModels.LogInViewModel
import com.example.domo.viewModels.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private val viewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.data.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        CoroutineScope(IO).launch {
            delay(3000)
            withContext(Main) {
                viewModel.data.value = "newValue"
            }
        }
    }
}