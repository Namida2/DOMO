package com.example.domo.views.dialogs

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import entities.interfaces.OnDismissListener


class OrderMenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener,
) : BottomSheetDialogFragment() {

//    private var binding: DialogOrderMenuBinding? = null
//    private val viewModel: OrderMenuDialogViewModel by viewModels { ViewModelFactory }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        binding = DialogOrderMenuBinding.inflate(inflater, container, false)
//        setListeners()
//        observeViewModelStates()
//        return binding?.root
//    }
//
//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        onDismissListener.onDismiss()
//    }
//
//    private fun setListeners() {
//        with(binding!!) {
//            acceptOrderButton.setOnClickListener {
//                if (requireContext().isNetworkConnected()) {
//                    viewModel.onConfirmOrderButtonClick(acceptOrderButton)
//                } else requireContext().createMessageDialog(networkConnectionMessage)
//            }
//        }
//    }
//
//    private fun observeViewModelStates() {
//        viewModel.state.observe(viewLifecycleOwner) {
//            when (it) {
//                is OrderMenuDialogVMStates.InsertingCurrentOrder -> {
//                    ProcessAlertDialog.show(childFragmentManager, "")
//                }
//                is OrderMenuDialogVMStates.InsertingWasSuccessful -> {
//                    ProcessAlertDialog.onSuccess()
//                }
//                is OrderMenuDialogVMStates.InsertingWasFailure -> {
//                    requireContext().createMessageDialog(it.errorMasse)
//                }
//                else -> {}//Default state
//            }
//        }
//    }
}