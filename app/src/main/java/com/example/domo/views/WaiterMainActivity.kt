package com.example.domo.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import com.example.domo.R
import com.example.domo.databinding.ActivityWaiterMainBinding

class WaiterMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityWaiterMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaiterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Add shared ViewModel

        //TODO: Study "8. Add Container Transform transition from email address chip to card view" in codeLabs
    }
}