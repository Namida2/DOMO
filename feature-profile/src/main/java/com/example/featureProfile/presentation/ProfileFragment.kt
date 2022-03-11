package com.example.featureProfile.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.domain.tools.ErrorMessages.checkNetworkConnectionMessage
import com.example.core.domain.tools.dialogs.ClosedQuestionDialog
import com.example.core.domain.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.core.domain.tools.extensions.isNetworkConnected
import com.example.featureProfile.R
import com.example.featureProfile.databinding.FragmentProfileBinding
import com.example.featureProfile.domain.ViewModelFactory
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.android.material.transition.platform.MaterialSharedAxis

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory }
    private lateinit var closedQuestionDialog: ClosedQuestionDialog<Unit>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        closedQuestionDialog = ClosedQuestionDialog(
            ProfileDepsStore.deps.currentEmployee?.name,
            resources.getString(R.string.leaveAccountMessage)
        ) { //TODO: leaveThisAccount //STOPPED//
            viewModel.leaveThisAccount()
        }
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        initBinding()
        observeViewModelStates()
        return binding.root
    }

    private fun initBinding() {
        binding.leaveAccountButton.setOnClickListener {
            if (requireContext().isNetworkConnected()) {
                closedQuestionDialog.show(parentFragmentManager, "")
            } else requireContext().createMessageDialog(checkNetworkConnectionMessage)
                ?.show(parentFragmentManager, "")
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ProfileViewModelStates.TryingToLogOut ->
                    ProcessAlertDialog.show(parentFragmentManager, "")
                is ProfileViewModelStates.LogOutFailed -> {
                    ProcessAlertDialog.dismiss()
                    requireContext().createMessageDialog(it.errorMessage)
                        ?.show(parentFragmentManager, "")
                }
                is ProfileViewModelStates.LogOutWasSuccessful -> {
                    ProcessAlertDialog.onSuccess()
                    //TODO: Launch the SplashScreenActivity

                }
                is ProfileViewModelStates.Default -> {}
            }
        }
    }
}