package com.example.featureOrder.presentation.order.doalogs.orderMenuDialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.featureOrder.databinding.DialogOrderMenuBinding
import com.example.featureOrder.domain.ViewModelFactory
import com.example.featureOrder.domain.interfaces.OnDismissListener
import com.example.waiterCore.domain.tools.ErrorMessages.networkConnectionMessage
import com.example.waiterCore.domain.tools.dialogs.ProcessAlertDialog
import com.example.waiterCore.domain.tools.extensions.createMessageDialog
import com.example.waiterCore.domain.tools.extensions.isNetworkConnected
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderMenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener,
) : BottomSheetDialogFragment() {

    private var binding: DialogOrderMenuBinding? = null
    private val viewModel: OrderMenuDialogViewModel by viewModels { ViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogOrderMenuBinding.inflate(inflater, container, false)
        setListeners()
        observeViewModelStates()
        return binding?.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.onDismiss()
    }

    private fun setListeners() {
        with(binding!!) {
            acceptOrderButton.setOnClickListener {
                if (requireContext().isNetworkConnected()) {
                    viewModel.onConfirmOrderButtonClick(acceptOrderButton)
                } else requireContext().createMessageDialog(networkConnectionMessage)
            }
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is OrderMenuDialogVMStates.InsertingCurrentOrder -> {
                    ProcessAlertDialog.show(childFragmentManager, "")
                }
                is OrderMenuDialogVMStates.InsertingWasSuccessful -> {
                    ProcessAlertDialog.onSuccess()
                }
                is OrderMenuDialogVMStates.InsertingWasFailure -> {
                    requireContext().createMessageDialog(it.errorMasse)
                }
                else -> {}//Default state
            }
        }
    }
}