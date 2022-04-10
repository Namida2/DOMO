package com.example.core.presentation.adminPasswordDialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.R
import com.example.core.databinding.DialogAdminPasswordBinding
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.constants.Messages.wrongAdminPasswordMessage
import com.example.core.domain.entities.tools.extensions.createMessageDialog

interface AdminPasswordDialogCallbacks {
    fun onCorrectPassword()
    fun onDialogCanceled()
}

class AdminPasswordDialog(
    private val correctPassword: String,
    private val dialogCallback: AdminPasswordDialogCallbacks
) : DialogFragment() {

    private lateinit var binding: DialogAdminPasswordBinding
    private val viewModel by viewModels<AdminPasswordViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminPasswordViewModel(correctPassword) as T
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DialogAdminPasswordBinding.inflate(layoutInflater).also { it.viewModel = viewModel }
        val builder = AlertDialog.Builder(context, R.style.alertDialogStyle)
        observeOnPasswordCheckedEvent()
        return builder.setView(binding.root).create()
    }

    private fun observeOnPasswordCheckedEvent() {
        viewModel.onPasswordCheckedEvent.observe(this) {
            val isCorrectPassword = it.getData() ?: return@observe
            if (isCorrectPassword) {
                dialogCallback.onCorrectPassword()
                dismiss()
            } else requireContext().createMessageDialog(wrongAdminPasswordMessage)
                ?.show(parentFragmentManager, "")
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialogCallback.onDialogCanceled()
    }
}