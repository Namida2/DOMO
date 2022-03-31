package com.example.featureMenuDialog.presentation.editMenuItemDialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.tools.constants.Messages.emptyFieldMessage
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogEditMenuItemBinding
import com.example.featureMenuDialog.domain.tools.EditMenuDialogModes

class EditMenuItemDialog(
    private val mode: EditMenuDialogModes,
    private var dish: Dish? = null
) : DialogFragment() {

    private var categoryName: String? = null
    private lateinit var binding: DialogEditMenuItemBinding
    private val viewModel by viewModels<EditMenuItemViewModel>()

    constructor(
        mode: EditMenuDialogModes,
        categoryName: String,
    ) : this(mode) {
        this.categoryName = categoryName
    }

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
                    requireContext().createMessageDialog(it.message)
                        ?.show(parentFragmentManager, "")
                }
                is EditMenuItemVMState.ItemAdded -> {
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
                    EditMenuDialogModes.ADD_DISH -> {
                        with(binding) {
                            val menuItemName = menuItemName.text.toString()
                            val dishCost = dishCost.text.toString()
                            val dishWeight = dishWeight.text.toString()
                            if(isEmptyField(menuItemName, dishCost, dishWeight)) {
                                requireContext().createMessageDialog(
                                    emptyFieldMessage
                                )?.show(parentFragmentManager, "")
                                return@setOnClickListener
                            }
                            viewModel.addDish(
                                categoryName!!, menuItemName, dishCost, dishWeight
                            )
                        }
                    }
                    //Add this part //STOPPED//
                    EditMenuDialogModes.EDIT_DISH -> {
//                        viewModel.addDish()
                    }
                }
            }
        }
        initView()
    }

    private fun initView() {
        with(binding) {
            when (mode) {
                EditMenuDialogModes.EDIT_DISH -> {
                    menuItemName.setText(dish?.name)
                    dishCost.setText(dish?.cost)
                    dishWeight.setText(dish?.weight)
                }
                EditMenuDialogModes.ADD_DISH -> {}
            }
        }
    }

}
