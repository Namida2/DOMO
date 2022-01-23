package com.example.domo.authorization.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.domo.databinding.FragmentLogInBinding

import com.example.domo.splashScreen.presentation.SplashScreenActivity
import com.example.waiter_core.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiter_core.domain.tools.dialogs.ProcessAlertDialog
import com.example.waiter_core.domain.tools.extensions.createMessageDialog
import com.example.waiter_core.domain.tools.extensions.isNetworkConnected

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    //TODO: Add a viewModelFactory
    private lateinit var viewModel: LogInViewModel

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
                    viewModel.logIn(email.text.toString(), password.text.toString())
                } else requireContext().createMessageDialog(
                   networkConnectionMessage
                )?.show(parentFragmentManager, "")
            }
            newAccountButton.setOnClickListener {
                //findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
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
                    ProcessAlertDialog.onSuccess()
                    requireContext().startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
                }
                else -> {
                    if (it is LogInViewModelStates.Default) return@observe
                    ProcessAlertDialog.dismiss()
                    requireContext().createMessageDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }
}