package com.example.featureSettings.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.domain.interfaces.OrdersService
import com.example.featureMenuDialog.domain.MenuDialogDeps
import com.example.featureMenuDialog.domain.MenuDialogDepsStore
import com.example.featureMenuDialog.presentation.menuDialog.MenuBottomSheetDialog
import com.example.featureSettings.R
import com.example.featureSettings.data.services.WorkWithMenuService
import com.example.featureSettings.databinding.FragmentSettingsBinding
import com.example.featureSettings.domain.ViewModelFactory
import com.example.featureSettings.domain.di.SettingsDepsStore.appComponent
import com.example.featureSettings.domain.di.SettingsDepsStore.deps
import com.google.android.material.transition.platform.MaterialSharedAxis

class SettingsFragment: Fragment() {

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
        WorkWithMenuService.saveMenuUseCase = appComponent.provideSaveMenuUseCase()
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
        observeOnSaveMenuEvent()
        return binding.root
    }

    private fun initBinding() {
        with(binding) {
            editMenuCard.setOnClickListener {
                this@SettingsFragment.viewModel.saveMenuBeforeChanges()
                MenuBottomSheetDialog(
                    this@SettingsFragment.viewModel,
                    this@SettingsFragment.viewModel::saveNewMenuFromMenuBottomSheetDialog
                ).showIfNotAdded(parentFragmentManager, "")
            }
            maxTablesCount.setText(deps.settings.tablesCount.toString())
            maxGuestsCount.setText(deps.settings.guestsCount.toString())
            maxTablesCount.addTextChangedListener {
                this@SettingsFragment.viewModel.onSettingsChanged(
                    it.toString(),
                    binding.maxGuestsCount.text.toString()
                )
            }
            maxGuestsCount.addTextChangedListener {
                this@SettingsFragment.viewModel.onSettingsChanged(
                    binding.maxGuestsCount.text.toString(),
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
                get() = deps.currentEmployee
            override val ordersService: OrdersService
                get() = deps.ordersService
        }
    }

    private fun observeOnMenuDialogDismissEvent() {
        viewModel.onMenuDialogDismissEvent.observe(viewLifecycleOwner) {
            it.getData() ?: return@observe
            closedQuestionDialog.showIfNotAdded(parentFragmentManager, "")
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is SettingsVMStates.InProcess -> {
                    ProcessAlertDialog.showIfNotAdded(parentFragmentManager, "")
                }
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

    private fun observeOnSaveMenuEvent() {
        viewModel.onSaveMenuEvent.observe(viewLifecycleOwner) {
            val saveMenuData = it.getData() ?: return@observe
            WorkWithMenuService.targetCollectionRef = saveMenuData.targetCollectionRef
            WorkWithMenuService.simpleTask = saveMenuData.simpleTask
            val intent = Intent(requireContext(), WorkWithMenuService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                requireContext().startForegroundService(intent)
            else requireContext().startService(intent)
        }
    }
}