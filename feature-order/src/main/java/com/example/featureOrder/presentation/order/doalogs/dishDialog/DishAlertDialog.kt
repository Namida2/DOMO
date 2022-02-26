package com.example.featureOrder.presentation.order.doalogs.dishDialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.featureOrder.R
import com.example.featureOrder.databinding.DialogDishBinding
import com.example.featureOrder.domain.ViewModelFactory
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.tools.extensions.createMessageDialog

class DishAlertDialog : DialogFragment() {

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
            else -> {
            } //Default state
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.orderItem = dish
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
        initBinding()
        builder.setView(binding!!.root)
        viewModel.state.observeForever(observer)
        return builder.create()
    }

    private fun initBinding() {
        binding = DialogDishBinding.inflate(layoutInflater)
        binding?.viewModel = viewModel
        initView(binding!!)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.state.removeObserver(observer)
    }

    private fun initView(binding: DialogDishBinding) {
        binding.dishName.text = dish?.name
    }

}