package com.example.domo.views.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domo.adapters.GuestsCountAdapter
import com.example.domo.databinding.DialogGuestCountBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GuestsCountBottomSheetDialog(
    private val onCountSelected: (count: Int) -> Unit
): BottomSheetDialogFragment() {

    private var binding: DialogGuestCountBinding? = null

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
        binding = DialogGuestCountBinding.inflate(layoutInflater, container, false)
        with(binding!!.guestsCountRecyclerView) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GuestsCountAdapter(16) {
                onCountSelected(it)
                this@GuestsCountBottomSheetDialog.dismiss()
            }
        }

    }
}