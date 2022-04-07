package com.example.featureProfile.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.workers.NewOrderItemStatusWorker
import com.example.core.data.workers.NewOrdersWorker
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.constants.OtherStringConstants.ACTIVITY_IS_NOT_LEAVE_ACCOUNT_CALLBACK
import com.example.core.domain.entities.tools.dialogs.ClosedQuestionDialog
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.dismissIfAdded
import com.example.core.domain.entities.tools.extensions.isNetworkConnected
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.domain.interfaces.LeaveAccountCallback
import com.example.featureProfile.R
import com.example.featureProfile.databinding.FragmentProfileBinding
import com.example.featureProfile.domain.ViewModelFactory
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.android.material.transition.platform.MaterialSharedAxis

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var leaveAccountCallback: LeaveAccountCallback
    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory }
    private lateinit var closedQuestionDialog: ClosedQuestionDialog<Unit>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        closedQuestionDialog = ClosedQuestionDialog(
            ProfileDepsStore.deps.currentEmployee?.name,
            resources.getString(R.string.leaveAccountMessage)
        ) {
            viewModel.leaveThisAccount()
        }
        leaveAccountCallback = (context as? LeaveAccountCallback)
            ?: throw IllegalArgumentException(ACTIVITY_IS_NOT_LEAVE_ACCOUNT_CALLBACK)
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
        val currentEmployee = ProfileDepsStore.deps.currentEmployee
        val switchState =
            NewOrdersWorker.needToShowNotifications || NewOrderItemStatusWorker.needToShowNotifications
        with(binding) {
            layoutProfile.employeeName.text = currentEmployee?.name
            layoutProfile.employeeEmail.text = currentEmployee?.email
            layoutProfile.employeePost.text = currentEmployee?.post
            leaveAccountButton.setOnClickListener {
                onLeaveAccount()
            }
            notificationsSwitch.isChecked = switchState
            notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
                onSwitchStateChanged(isChecked)
            }
        }
    }

    private fun onSwitchStateChanged(isChecked: Boolean) {
        NewOrdersWorker.needToShowNotifications = isChecked
        NewOrderItemStatusWorker.needToShowNotifications = isChecked
    }

    private fun onLeaveAccount() {
        if (requireContext().isNetworkConnected()) {
            closedQuestionDialog.showIfNotAdded(parentFragmentManager, "")
        } else requireContext().createMessageDialog(checkNetworkConnectionMessage)
            ?.show(parentFragmentManager, "")
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ProfileViewModelStates.TryingToLogOut ->
                    ProcessAlertDialog.showIfNotAdded(parentFragmentManager, "")
                is ProfileViewModelStates.LogOutFailed -> {
                    ProcessAlertDialog.dismissIfAdded()
                    requireContext().createMessageDialog(it.errorMessage)
                        ?.show(parentFragmentManager, "")
                }
                is ProfileViewModelStates.LogOutWasSuccessful -> {
                    ProcessAlertDialog.dismissIfAdded()
                    leaveAccountCallback.onLeaveAccount()
                }
                is ProfileViewModelStates.Default -> {
                    ProcessAlertDialog.dismissIfAdded()
                }
            }
        }
    }
}