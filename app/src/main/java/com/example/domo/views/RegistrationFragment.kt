package com.example.domo.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import application.appComponent
import com.example.domo.databinding.DialogErrorBinding
import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.viewModels.RegistrationViewModel
import com.example.domo.viewModels.RegistrationViewModelStates
import com.example.domo.viewModels.ViewModelFactory
import tools.ErrorMessage
import tools.MyAlertDialog


class RegistrationFragment : Fragment() {

    private var viewModel: RegistrationViewModel? = null
    lateinit var binding: FragmentRegistrationBinding
    lateinit var errorDialogBinding: DialogErrorBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(context.appComponent)).get(
                RegistrationViewModel::class.java
            )
    }

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
        viewModel?.cookPostSelectedIcon?.observe(viewLifecycleOwner) {
            log(it.toString())
        }

        return binding.root
    }

    private fun observeViewModelState() {
        viewModel?.state?.observe(viewLifecycleOwner) {
            var dialog: DialogFragment? = null
            when (it) {
                is RegistrationViewModelStates.Validating -> binding.registrationButton.isEnabled =
                    false
                is RegistrationViewModelStates.WrongPasswordConfirmation -> dialog = createDialog(it.message)
                is RegistrationViewModelStates.EmptyField -> dialog = createDialog(it.message)
                is RegistrationViewModelStates.ShortPassword -> dialog = createDialog(it.message)
                is RegistrationViewModelStates.InvalidEmail -> dialog = createDialog(it.message)
                is RegistrationViewModelStates.Valid -> {
                    viewModel?.registration(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString(),
                        it.employee
                    )
                }
                else -> {
                } //DefaultState
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