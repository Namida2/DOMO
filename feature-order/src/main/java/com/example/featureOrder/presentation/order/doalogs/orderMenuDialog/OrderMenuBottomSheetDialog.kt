package com.example.featureOrder.presentation.order.doalogs.orderMenuDialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.presentation.ProcessAleartDialog.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.dismissIfAdded
import com.example.core.domain.entities.tools.extensions.isNetworkConnected
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureOrder.databinding.DialogOrderMenuBinding
import com.example.featureOrder.domain.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderMenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener,
    private val onChangeGuestCount: () -> Unit
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
                    viewModel.onConfirmOrderButtonClick()
                } else {
                    requireContext().createMessageDialog(checkNetworkConnectionMessage)
                        ?.show(parentFragmentManager, "")
                    dismiss()
                }
            }
            changeGuestCountButton.setOnClickListener {
                onChangeGuestCount.invoke()
                dismiss()
            }
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is OrderMenuDialogVMStates.InsertingCurrentOrder -> {
                    ProcessAlertDialog.showIfNotAdded(childFragmentManager, "")
                }
                is OrderMenuDialogVMStates.InsertingWasSuccessful -> {
                    ProcessAlertDialog.onSuccess()
                }
                is OrderMenuDialogVMStates.InsertingWasFailure -> {
                    requireContext().createMessageDialog(it.errorMessage) {
                        ProcessAlertDialog.dismissIfAdded()
                    }?.show(parentFragmentManager, "")
                }
                is OrderMenuDialogVMStates.Default -> {
                    ProcessAlertDialog.dismissIfAdded()
                }
            }
        }
    }
}