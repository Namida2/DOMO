package com.example.featureOrder.presentation.order.doalogs.guestsCountDialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.featureOrder.databinding.DialogGuestsCountBinding
import com.example.featureOrder.presentation.order.adapters.GuestsCountAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GuestsCountBottomSheetDialog(
    private val onCountSelected: (count: Int) -> Unit
): BottomSheetDialogFragment() {

    private var binding: DialogGuestsCountBinding? = null
    private val defaultMaxCount = 16

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initBinding(container)
        return binding?.root
    }

    private fun initBinding(container: ViewGroup?,) {
        binding = DialogGuestsCountBinding.inflate(layoutInflater, container, false)
        with(binding!!.guestsCountRecyclerView) {
            layoutManager =
                LinearLayoutManager(requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
            adapter = GuestsCountAdapter(defaultMaxCount) {
                onCountSelected(it)
                this@GuestsCountBottomSheetDialog.dismiss()
            }
        }
    }
}