package com.example.domo.views.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.domo.databinding.DialogOrderMenuBinding
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.dialogs.OrderMenuDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import extentions.appComponent


class OrderMenuBottomSheetDialog : BottomSheetDialogFragment() {

    private var binding: DialogOrderMenuBinding? = null
    private lateinit var viewModel: OrderMenuDialogViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity(),
            ViewModelFactory(context.appComponent))[OrderMenuDialogViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogOrderMenuBinding.inflate(inflater, container, false)
        return binding?.root
    }
}