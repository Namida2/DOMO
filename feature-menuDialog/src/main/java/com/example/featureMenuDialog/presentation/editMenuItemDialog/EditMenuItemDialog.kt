package com.example.featureMenuDialog.presentation.editMenuItemDialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.core.domain.menu.Dish
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogEditMenuItemBinding
import com.example.featureMenuDialog.domain.ViewModelFactory
import com.example.featureMenuDialog.domain.tools.MenuItemsEnum

class EditMenuItemDialog(
    private val mode: MenuItemsEnum,
    private val categoryName: String? = null,
    private val dish: Dish? = null
) : DialogFragment() {

    private lateinit var binding: DialogEditMenuItemBinding
    private val viewModel by viewModels<EditMenuItemViewModel> { ViewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
        initBinding()
        builder.setView(binding.root)
        observeViewModelState()
        return builder.create()
    }

    private fun observeViewModelState() {
        viewModel.state.observe(this) {
            when (it) {
                is EditMenuItemVMState.ItemAlreadyExists -> {
                    viewModel.resetState()
                    requireContext().createMessageDialog(it.message)
                        ?.show(parentFragmentManager, "")
                }
                is EditMenuItemVMState.ItemAdded -> {
                    viewModel.resetState()
                    this.dismiss()
                }
                EditMenuItemVMState.Default -> {}
            }
        }
    }

    private fun initBinding() {
        binding = DialogEditMenuItemBinding.inflate(layoutInflater).also {
            it.confirmButton.setOnClickListener {
                when (mode) {
                    MenuItemsEnum.CATEGORY -> {
                        viewModel.addCategory(binding.itemName.text.toString())
                    }
                    MenuItemsEnum.DISH -> {
                        //TODO: Complete this part //STOPPED//
                        viewModel.addDish()
                    }
                }
            }
        }
        initView()
    }

    private fun initView() {
        with(binding) {
            when (mode) {
                MenuItemsEnum.CATEGORY -> {
                    cost.visibility = View.GONE
                    weight.visibility = View.GONE
                }
                MenuItemsEnum.DISH -> {
                    itemName.setText(dish?.name)
                    cost.setText(dish?.cost)
                    weight.setText(dish?.weight)
                }
            }
        }
    }

}