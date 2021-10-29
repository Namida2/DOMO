package com.example.domo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.domo.R
import com.example.domo.databinding.FragmentLogInBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tools.dialogs.ProcessAlertDialog

class LogInFragment: Fragment() {

    private lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        binding.newAccountButton .setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
        }
        return binding.root
    }
}