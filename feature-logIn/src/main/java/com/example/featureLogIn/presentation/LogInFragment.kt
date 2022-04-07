package com.example.featureLogIn.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.constants.OtherStringConstants.ACTIVITY_IS_NOT_EMPLOYEE_AUTHORIZATION_CALLBACK
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.dismissIfAdded
import com.example.core.domain.entities.tools.extensions.isNetworkConnected
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.featureLogIn.R
import com.example.featureLogIn.databinding.FragmentLogInBinding
import com.example.featureLogIn.domain.ViewModelFactory
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureRegistration.domain.di.RegistrationAppComponentDeps
import com.example.featureRegistration.domain.di.RegistrationDepsStore
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {
    private lateinit var employeeAuthCallback: EmployeeAuthCallback
    private lateinit var binding: FragmentLogInBinding
    private val viewModel by viewModels<LogInViewModel> { ViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        employeeAuthCallback =
            (context as? EmployeeAuthCallback) ?: throw IllegalArgumentException(
                ACTIVITY_IS_NOT_EMPLOYEE_AUTHORIZATION_CALLBACK
            )
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
                prepareRegistrationScreen()
            }
        }
        observeViewModelStates()
    }

    private fun prepareRegistrationScreen() {
        RegistrationDepsStore.deps = object : RegistrationAppComponentDeps {
            override val firebaseAuth: FirebaseAuth
                get() = LogInDepsStore.deps.firebaseAuth
        }
        if(findNavController().currentDestination?.id == R.id.logInFragment)
            findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is LogInViewModelStates.Validating -> {
                    ProcessAlertDialog.showIfNotAdded(parentFragmentManager, "")
                }
                is LogInViewModelStates.Success -> {
                    ProcessAlertDialog.dismissIfAdded()
                    employeeAuthCallback.onAuthorisationEvent(it.employee)
                }
                else -> {
                    if (it is LogInViewModelStates.Default) return@observe
                    ProcessAlertDialog.dismissIfAdded()
                    if (it.errorMessage == null) return@observe
                    requireContext().createMessageDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }
}