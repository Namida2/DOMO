package com.example.domo.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.domo.R
import com.example.domo.databinding.FragmentLogInBinding
import com.example.domo.viewModels.LogInViewModel
import com.example.domo.viewModels.LogInViewModelStates
import com.example.domo.viewModels.ViewModelFactory
import entities.ErrorMessage
import extentions.appComponent
import extentions.createDialog
import extentions.isNetworkConnected
import tools.dialogs.ProcessAlertDialog

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private var viewModel: LogInViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(context.appComponent)).get(
                LogInViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel?.resetState()
        binding = FragmentLogInBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            logInButton.setOnClickListener {
                if (requireContext().isNetworkConnected()) {
                    viewModel?.signIn(email.text.toString(), password.text.toString())
                } else requireContext().createDialog(
                    ErrorMessage(
                        R.string.defaultTitle,
                        R.string.networkConnectionMessage
                    )
                )?.show(parentFragmentManager, "")
            }
            newAccountButton.setOnClickListener {
                findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
            }
        }

        viewModel?.state?.observe(viewLifecycleOwner) {
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
                    requireContext().createDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }


}