package com.example.domo.views

import Tools.ErrorAlertDialog
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.viewModels.RegistrationViewModel

import com.example.domo.databinding.DialogErrorBinding
import com.example.domo.viewModels.RegistrationViewModelStates


class RegistrationFragment: Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()
    lateinit var binding: FragmentRegistrationBinding
    lateinit var errorDialogBinding: DialogErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        errorDialogBinding = DialogErrorBinding.inflate(inflater)
        binding = FragmentRegistrationBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        observeViewModelState()

        return binding.root
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            var dialog: DialogFragment? = null
            when(it) {
                is RegistrationViewModelStates.WrongPasswordConfirmation -> {

                }
                is RegistrationViewModelStates.EmptyField -> {
                    dialog = ErrorAlertDialog.getNewInstance<Unit>(
                        errorDialogBinding,
                        requireContext().resources.getString(it.message.titleId),
                        requireContext().resources.getString(it.message.messageId) )
                }
                is RegistrationViewModelStates.ShortPassword -> {

                }
                else -> {}//defaultState
            }
            dialog?.show(parentFragmentManager, "")

        }
    }

}