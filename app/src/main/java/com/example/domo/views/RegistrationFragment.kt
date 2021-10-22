package com.example.domo.views

import tools.MyAlertDialog
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.domo.R

import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.viewModels.RegistrationViewModel

import com.example.domo.databinding.DialogErrorBinding
import com.example.domo.viewModels.RegistrationViewModelStates
import tools.ErrorMessage


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
                is RegistrationViewModelStates.Validating -> binding.registrationButton.isEnabled = false
                is RegistrationViewModelStates.WrongPasswordConfirmation -> createDialog(it.message)
                is RegistrationViewModelStates.EmptyField -> createDialog(it.message)
                is RegistrationViewModelStates.ShortPassword -> createDialog(it.message)
                is RegistrationViewModelStates.InvalidEmail -> createDialog(it.message)
                is RegistrationViewModelStates.Valid -> {

                }
                else -> {} //DefaultState
            }
            dialog?.show(parentFragmentManager, "")

        }
    }
    private fun createDialog(message: ErrorMessage): DialogFragment? =
        MyAlertDialog.getNewInstance<Unit>(
            errorDialogBinding,
            requireContext().resources.getString(message.titleId),
            requireContext().resources.getString(message.messageId)
        )



}