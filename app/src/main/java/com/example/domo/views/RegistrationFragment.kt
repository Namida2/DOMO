package com.example.domo.views

import Tools.ErrorAlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.viewModels.RegistrationViewModel
import android.content.DialogInterface


import android.app.AlertDialog
import com.example.domo.databinding.DialogErrorBinding
import com.example.domo.viewModels.RegistrationViewModelStates
import com.google.common.eventbus.Subscribe


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
            when(it) {
                is RegistrationViewModelStates.WrongPasswordConfirmation -> {

                }
                is RegistrationViewModelStates.EmptyField -> {
                    with(it) {
                        context?.let {
                            val dialog = ErrorAlertDialog.getInstance<Unit>(
                                requireContext(), errorDialogBinding,
                                requireContext().resources.getString(message.titleId),
                                requireContext().resources.getString(message.messageId)) {

                            }


                        }
                    }
                }
                else -> {}//defaultState
            }

        }
    }

}