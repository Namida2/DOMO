package com.example.authorisation.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.authorisation.R
import com.example.authorisation.databinding.FragmentLogInBinding
import com.example.waiter_core.domain.tools.ErrorMessage

class LogInFragment : Fragment() {

//    private lateinit var binding: FragmentLogInBinding
//    private lateinit var viewModel: LogInViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        viewModel.resetState()
//        binding = FragmentLogInBinding.inflate(layoutInflater)
//        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
//        setListeners()
//        return binding.root
//    }
//
//    private fun setListeners() {
//        with(binding) {
//            logInButton.setOnClickListener {
//                if (requireContext().isNetworkConnected()) {
//                    viewModel?.signIn(email.text.toString(), password.text.toString())
//                } else requireContext().createMessageDialog(
//                    ErrorMessage(
//                        R.string.defaultTitle,
//                        R.string.networkConnectionMessage
//                    )
//                )?.show(parentFragmentManager, "")
//            }
//            newAccountButton.setOnClickListener {
//                findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
//            }
//        }
//
//        viewModel.state.observe(viewLifecycleOwner) {
//            when (it) {
//                is LogInViewModelStates.Validating -> {
//                    ProcessAlertDialog.show(parentFragmentManager, "")
//                }
//                is LogInViewModelStates.Success -> {
//                    ProcessAlertDialog.onSuccess()
//                    requireContext().startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
//                }
//                else -> {
//                    if (it is LogInViewModelStates.Default) return@observe
//                    ProcessAlertDialog.dismiss()
//                    requireContext().createMessageDialog(it.errorMessage!!)
//                        ?.show(parentFragmentManager, "")
//                }
//            }
//        }
//    }
}