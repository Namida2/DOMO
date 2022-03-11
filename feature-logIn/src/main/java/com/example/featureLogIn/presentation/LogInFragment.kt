package com.example.featureLogIn.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessages.checkNetworkConnectionMessage
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.core.domain.tools.extensions.isNetworkConnected
import com.example.featureLogIn.R
import com.example.featureLogIn.databinding.FragmentLogInBinding
import com.example.featureLogIn.domain.ViewModelFactory
import com.example.featureLogIn.domain.interfaces.EmployeeAuthorizationCallback
import java.io.Serializable

class LogInFragment : Fragment() {
    private val args by navArgs<LogInFragmentArgs>()
    private lateinit var binding: FragmentLogInBinding
    private val viewModel by viewModels<LogInViewModel> { ViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.resetState()
        binding = FragmentLogInBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            logInButton.setOnClickListener {
                if (requireContext().isNetworkConnected()) {
                    viewModel.logIn(email.text.toString().lowercase(), password.text.toString())
                } else requireContext().createMessageDialog(
                    checkNetworkConnectionMessage
                )?.show(parentFragmentManager, "")
            }
            newAccountButton.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
            }
        }
        observeViewModelStates()
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is LogInViewModelStates.Validating -> {
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.show(
                        parentFragmentManager,
                        ""
                    )
                }
                is LogInViewModelStates.Success -> {
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.dismiss()
                    args.employeeAuthorizationCallback.onEmployeeLoggedIn(it.employee)
                }
                else -> {
                    if (it is LogInViewModelStates.Default) return@observe
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.dismiss()
                    if (it.errorMessage == null) return@observe
                    requireContext().createMessageDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }
}