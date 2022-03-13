package com.example.domo.views.fragments.authorisation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.core.domain.tools.extensions.isNetworkConnected
import com.example.domo.R
import com.example.featureSplashScreen.presentation.SplashScreenActivity
import com.example.domo.viewModels.LogInViewModel
import com.example.domo.viewModels.LogInViewModelStates
import com.example.domo.viewModels.ViewModelFactory
import com.example.featureLogIn.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private val viewModel: LogInViewModel by viewModels { ViewModelFactory }

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
                    viewModel.signIn(email.text.toString(), password.text.toString())
                } else requireContext().createMessageDialog(
                    com.example.core.domain.tools.ErrorMessage(
                        R.string.defaultTitle,
                        R.string.networkConnectionMessage
                    )
                )?.show(parentFragmentManager, "")
            }
            newAccountButton.setOnClickListener {
                //findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is LogInViewModelStates.Validating -> {
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.show(
                        parentFragmentManager,
                        ""
                    )
                }
                is LogInViewModelStates.Success -> {
//                    ProcessAlertDialog.onSuccess()
                    requireContext().startActivity(
                        Intent(
                            requireContext(),
                            SplashScreenActivity::class.java
                        )
                    )
                }
                else -> {
                    if (it is LogInViewModelStates.Default) return@observe
                    com.example.core.domain.tools.dialogs.ProcessAlertDialog.dismiss()
                    requireContext().createMessageDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }
}