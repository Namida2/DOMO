package com.example.featureCurrentOrders.presentation.currentOrddersDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersBinding

class CurrentOrdersDetailFragment: Fragment() {

    private lateinit var binding: FragmentCurrentOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }
}