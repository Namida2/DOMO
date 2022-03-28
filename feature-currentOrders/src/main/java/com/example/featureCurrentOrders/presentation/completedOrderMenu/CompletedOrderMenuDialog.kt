package com.example.featureCurrentOrders.presentation.completedOrderMenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.core.domain.order.Order
import com.example.core.presentation.recyclerView.itemDecorations.CompletedOrderVMStates
import com.example.core.presentation.recyclerView.itemDecorations.CompletedOrderViewModel
import com.example.featureCurrentOrders.databinding.DialogCompletedOrderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.callbackFlow

//TODO: Cool thing
abstract class CompletedOrderDialogCallback {
    abstract fun showDetail(order: Order)
    open fun onOrderDeleted() {}
}

class CompletedOrderMenuDialog(
    private val completedOrderDialogCallback: CompletedOrderDialogCallback
) : BottomSheetDialogFragment() {

    lateinit var order: Order
    private lateinit var bidning: DialogCompletedOrderBinding
    private val viewModel by viewModels<CompletedOrderViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bidning = DialogCompletedOrderBinding.inflate(inflater, container, false)
        bidning.viewModel = viewModel
        observeViewModelStates()
        return bidning.root
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            //TODO: Delete the selected order //STOPPED//
            when (it) {
                CompletedOrderVMStates.ShowDetail -> {
                    dismiss()
                    completedOrderDialogCallback.showDetail(order)
                }
                CompletedOrderVMStates.DeletingOrder -> {

                }
                CompletedOrderVMStates.OrderDeleted -> {

                }
                CompletedOrderVMStates.Default -> {

                }
            }
        }
    }

}