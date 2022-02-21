package com.example.domo.authorization.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domo.R
import com.example.domo.databinding.FragmentLogInBinding
import com.example.domo.splashScreen.domain.ViewModelFactory

import com.example.domo.splashScreen.presentation.SplashScreenActivity
import com.example.waiterCore.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiterCore.domain.tools.dialogs.ProcessAlertDialog
import com.example.waiterCore.domain.tools.extensions.createMessageDialog
import com.example.waiterCore.domain.tools.extensions.isNetworkConnected
import extentions.employee

class LogInFragment : Fragment() {
    //TODO: Implement the registration
    private lateinit var binding: FragmentLogInBinding
    private val viewModel by viewModels<LogInViewModel> { ViewModelFactory  }

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
                   networkConnectionMessage
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
                    ProcessAlertDialog.show(parentFragmentManager, "")
                }
                is LogInViewModelStates.Success -> {
                    ProcessAlertDialog.dismiss()
                    requireActivity().employee = it.employee
                    requireContext().startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
                }
                else -> {
                    if (it is LogInViewModelStates.Default) return@observe
                    ProcessAlertDialog.dismiss()
                    if (it.errorMessage == null) return@observe
                    requireContext().createMessageDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }
}