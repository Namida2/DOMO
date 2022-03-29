package com.example.featureMenuDialog.presentation.addCategoryDialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogEditMenuItemBinding

class AddCategoryDialog: DialogFragment() {

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
                title.text = resources.getString(R.string.addNewCategoryTitle)
                dishCost.visibility = View.GONE
                dishWeight.visibility = View.GONE
                confirmButton.setOnClickListener {
                    viewModel.addCategory(menuItemName.text.toString())
                }
            }
        }
        this.initBinding()
    }

}