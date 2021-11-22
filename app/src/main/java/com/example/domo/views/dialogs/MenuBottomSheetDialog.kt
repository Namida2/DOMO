package com.example.domo.views.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.R
import com.example.domo.adapters.MenuAdapter
import com.example.domo.databinding.DialogMenuBinding
import com.example.domo.models.interfaces.MenuHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.transition.MaterialContainerTransform
import entities.recyclerView.DishMenuRecyclerViewType
import extentions.appComponent
import javax.inject.Inject

class MenuBottomSheetDialog: BottomSheetDialogFragment() {

    lateinit var binding: DialogMenuBinding
    @Inject
    lateinit var menuHolder: MenuHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //TODO: Add a MenuViwModel
        requireContext().appComponent.inject(this)
        binding = DialogMenuBinding.inflate(layoutInflater)
        with(binding.menuRecyclerView) {
            adapter = MenuAdapter(listOf(DishMenuRecyclerViewType())).apply {
                submitList(
                    menuHolder.menu[1].dishes
                )
            }
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
        return binding.root
    }

    override fun getView(): View = binding.root
}