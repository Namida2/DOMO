package com.example.featureSettings.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.dialogs.ClosedQuestionDialog
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideDownFomTop
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideUp
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.dismissIfAdded
import com.example.core.domain.interfaces.OrdersService
import com.example.featureMenuDialog.domain.MenuDialogDeps
import com.example.featureMenuDialog.domain.MenuDialogDepsStore
import com.example.featureMenuDialog.presentation.menuDialog.MenuBottomSheetDialog
import com.example.featureSettings.R
import com.example.featureSettings.databinding.FragmentSettingsBinding
import com.example.featureSettings.domain.ViewModelFactory
import com.example.featureSettings.domain.di.SettingsDepsStore
import com.example.featureSettings.domain.di.SettingsDepsStore.deps
import com.google.android.material.transition.platform.MaterialSharedAxis

// TODO: Work with menu in foreground service //STOPPED//
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
        binding = FragmentSettingsBinding
            .inflate(layoutInflater, container, false).also { it.viewModel = viewModel }
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
        observeOnSettingChangedEvent()
        return binding.root
    }

    private fun initBinding() {
        with(binding) {
            editMenuCard.setOnClickListener {
                this@SettingsFragment.viewModel.saveMenuBeforeChanges()
                MenuBottomSheetDialog(this@SettingsFragment.viewModel).show(
                    parentFragmentManager,
                    ""
                )
            }
            maxTablesCount.setText(deps.settings.tablesCount.toString())
            maxGuestsCount.setText(deps.settings.guestsCount.toString())
            maxTablesCount.addTextChangedListener {
                this@SettingsFragment.viewModel.onMaxTablesCountChanged(
                    it.toString()
                )
            }
            maxGuestsCount.addTextChangedListener {
                this@SettingsFragment.viewModel.onMaxGuestCountChanged(
                    it.toString()
                )
            }
        }
    }

    private fun observeOnSettingChangedEvent() {
        viewModel.onSettingChangedEvent.observe(viewLifecycleOwner) {
            val button = binding.saveSettingsButton
            if (it.getData() ?: return@observe) {
                if (button.visibility == View.VISIBLE) return@observe
                button.visibility = View.VISIBLE
                button.prepareSlideDownFomTop(button.height).start()
            } else button.prepareSlideUp(button.height) {
                button.visibility = View.GONE
            }.start()
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
                is SettingsVMStates.InProcess ->
                    ProcessAlertDialog.show(parentFragmentManager, "")
                is SettingsVMStates.OnFailure -> {
                    ProcessAlertDialog.dismissIfAdded()
                    requireContext().createMessageDialog(it.message)
                        ?.show(parentFragmentManager, "")
                }
                is SettingsVMStates.OnSuccess ->
                    ProcessAlertDialog.onSuccess()
                is SettingsVMStates.Default -> {
                    ProcessAlertDialog.dismissIfAdded()
                }
            }
        }

    }
}