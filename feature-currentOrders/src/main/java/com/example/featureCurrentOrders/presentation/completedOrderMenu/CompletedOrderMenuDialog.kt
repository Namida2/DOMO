package com.example.featureCurrentOrders.presentation.completedOrderMenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.featureCurrentOrders.databinding.DialogCompletedOrderBinding
import com.example.featureCurrentOrders.domain.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//TODO: Cool thing
abstract class CompletedOrderDialogCallback {
    abstract fun showDetail(order: Order)
    open fun onOrderDeleted() {}
}

class CompletedOrderMenuDialog(
    private val completedOrderDialogCallback: CompletedOrderDialogCallback
) : BottomSheetDialogFragment() {

    lateinit var order: Order
    private lateinit var binding: DialogCompletedOrderBinding
    private val viewModel by viewModels<CompletedOrderViewModel> { ViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.orderId = order.orderId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCompletedOrderBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        observeViewModelStates()
        return binding.root
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is CompletedOrderVMStates.ShowDetail -> {
                    dismiss()
                    completedOrderDialogCallback.showDetail(order)
                }
                is CompletedOrderVMStates.DeletingOrder -> {
                    ProcessAlertDialog.showIfNotAdded(parentFragmentManager, "")
                }
                is CompletedOrderVMStates.OrderDeleted -> {
                    ProcessAlertDialog.onSuccess()
                }
                is CompletedOrderVMStates.OnOrderDeletingFailure -> {
                    ProcessAlertDialog.dismiss()
                    requireContext().createMessageDialog(it.errorMessage)
                        ?.show(parentFragmentManager, "")
                }
                is CompletedOrderVMStates.Default -> {
                    if (!ProcessAlertDialog.isAdded) return@observe
                    ProcessAlertDialog.onSuccess()
                }
            }
        }
    }

}