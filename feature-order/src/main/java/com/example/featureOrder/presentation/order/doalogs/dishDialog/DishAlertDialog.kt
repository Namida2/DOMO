package com.example.featureOrder.presentation.order.doalogs.dishDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.featureOrder.R
import com.example.featureOrder.databinding.DialogDishBinding
import com.example.featureOrder.domain.ViewModelFactory
import com.example.featureOrder.presentation.order.doalogs.viewModels.DishDialogVMStates
import com.example.featureOrder.presentation.order.doalogs.viewModels.DishDialogViewModel
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.tools.extensions.createMessageDialog

//TODO: Add this dialog //STOPPED//
class DishAlertDialog : DialogFragment() {

    private val maxCountValue = 20
    private val minCountValue = 1

    var dish: Dish? = null

    private var binding: DialogDishBinding? = null
    private val viewModel: DishDialogViewModel by viewModels { ViewModelFactory }
    private val observer = Observer<DishDialogVMStates> {
        when (it) {
            is DishDialogVMStates.DishAlreadyAdded -> {
                requireContext().createMessageDialog(it.errorMessage!!)
                    ?.show(parentFragmentManager, "")
            }
            is DishDialogVMStates.DishSuccessfulAdded -> {
                this.dismiss()
            }
            else -> {} //Default state
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.dish = dish
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
//        initBinding()
        builder.setView(binding!!.root)
        viewModel.state.observeForever(observer)
        return builder.create()
    }

//    private fun initBinding() {
//        binding = DialogDishBinding.inflate(layoutInflater)
//        binding?.viewModel = viewModel
//        initViews(binding!!)
//        initViews(binding!!)
//    }

//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        viewModel.state.removeObserver(observer)
//    }
//
//    private fun initViews(binding: DialogDishBinding) {
//        binding.dishName.text = dish?.name
//        binding.countNumberPicker.maxValue = maxCountValue
//        binding.countNumberPicker.minValue = minCountValue
//    }

}