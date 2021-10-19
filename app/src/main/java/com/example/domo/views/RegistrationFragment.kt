package com.example.domo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.viewModels.RegistrationViewModel
import android.content.DialogInterface


import android.app.AlertDialog


class RegistrationFragment: Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()
    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = layoutInflater

        builder.setView(inflater.inflate(com.example.domo.R.layout.fragment_log_in, null)) // Add action buttons
            .setPositiveButton("com.example.domo.R.string.signin", DialogInterface.OnClickListener { dialog, id ->
                // sign in the user ...
            })
            .setNegativeButton(
                "com.example.domo.R.string.cancel",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create().show()

        return binding.root
    }


}