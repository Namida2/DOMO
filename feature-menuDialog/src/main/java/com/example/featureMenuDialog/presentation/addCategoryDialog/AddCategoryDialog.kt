package com.example.featureMenuDialog.presentation.addCategoryDialog

import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.tools.constants.Messages
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogEditMenuItemBinding


class AddCategoryDialog: DialogFragment() {

    private val maxLength = 20
    private lateinit var binding: DialogEditMenuItemBinding
    private val viewModel by viewModels<AddCategoryViewModel>()

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
                is AddCategoryVMStates.CategoryAlreadyExists -> {
                    requireContext().createMessageDialog(it.message)
                        ?.show(parentFragmentManager, "")
                }
                is AddCategoryVMStates.CategoryAdded -> {
                    this.dismiss()
                }
                AddCategoryVMStates.Default -> {}
            }
        }
    }

    private fun initBinding() {
        binding = DialogEditMenuItemBinding.inflate(layoutInflater).also {
            with(it) {
                val filterArray = arrayOfNulls<InputFilter>(1)
                filterArray[0] = LengthFilter(maxLength)
                menuItemName.filters = filterArray
                title.text = resources.getString(R.string.addNewCategoryTitle)
                dishCost.visibility = View.GONE
                dishWeight.visibility = View.GONE
                confirmButton.setOnClickListener {
                    if (isEmptyField(menuItemName.text.toString())) {
                        requireContext().createMessageDialog(
                            Messages.emptyFieldMessage
                        )?.show(parentFragmentManager, "")
                        return@setOnClickListener
                    }
                    viewModel.addCategory(menuItemName.text.toString())
                }
            }
        }
    }

}