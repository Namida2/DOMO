package com.example.featureSettings.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.OrdersServiceSub
import com.example.core.domain.entities.tools.dialogs.ClosedQuestionDialog
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.featureMenuDialog.domain.MenuDialogDeps
import com.example.featureMenuDialog.domain.MenuDialogDepsStore
import com.example.featureMenuDialog.presentation.menuDialog.MenuBottomSheetDialog
import com.example.featureSettings.R
import com.example.featureSettings.databinding.FragmentSettingsBinding
import com.example.featureSettings.domain.ViewModelFactory
import com.example.featureSettings.domain.di.SettingsDepsStore
import com.google.android.material.transition.platform.MaterialSharedAxis

// TODO: Listen menu version changes //STOPPED//
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var closedQuestionDialog: ClosedQuestionDialog<Unit>
    private val viewModel by viewModels<SettingsViewModel> { ViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        closedQuestionDialog = ClosedQuestionDialog(
            resources.getString(R.string.saveMenuChangesTitle),
            resources.getString(R.string.saveMenuChangesMessage),
            { viewModel.onCancelNewMenu() },
            { viewModel.onAcceptNewMenu() }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        initBinding()
        provideMenuDialogDeps()
        observeOnMenuDialogDismissEvent()
        observeViewModelStates()
        return binding.root
    }

    private fun initBinding() {
        binding.editMenuCard.setOnClickListener {
            viewModel.sameMenuBeforeChanges()
            MenuBottomSheetDialog(viewModel).show(parentFragmentManager, "")
        }
    }

    private fun provideMenuDialogDeps() {
        MenuDialogDepsStore.deps = object : MenuDialogDeps {
            override val currentEmployee: Employee?
                get() = SettingsDepsStore.deps.currentEmployee
            override val ordersService: OrdersService
                get() = SettingsDepsStore.deps.ordersService

        }
    }

    private fun observeOnMenuDialogDismissEvent() {
        viewModel.onMenuDialogDismissEvent.observe(viewLifecycleOwner) {
            it.getData() ?: return@observe
            closedQuestionDialog.show(parentFragmentManager, "")
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is SettingsVMStates.SavingMenu ->
                    ProcessAlertDialog.show(parentFragmentManager, "")
                is SettingsVMStates.OnSavingFailed -> {
                    ProcessAlertDialog.dismiss()
                    requireContext().createMessageDialog(it.message)
                        ?.show(parentFragmentManager, "")
                }
                is SettingsVMStates.OnSavingSuccess ->
                    ProcessAlertDialog.onSuccess()
                is SettingsVMStates.Default -> {}
            }
        }

    }
}