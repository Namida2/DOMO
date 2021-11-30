package com.example.domo.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.domo.R
import com.example.domo.databinding.DialogDishBinding
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.dialogs.DishDialogVMStates
import com.example.domo.viewModels.dialogs.DishDialogViewModel
import entities.menu.Dish
import extentions.appComponent
import extentions.createDialog

class DishAlertDialog : DialogFragment() {

    private var tableId: Int = 0
    private var guestCount: Int = 0

    private val maxCountValue = 20
    private val minCountValue = 1

    var dish: Dish? = null

    private var binding: DialogDishBinding? = null
    private lateinit var viewModel: DishDialogViewModel
    private val observer = Observer<DishDialogVMStates> {
        when(it) {
            is DishDialogVMStates.DishAlreadyAdded -> {
                requireContext().createDialog(it.errorMessage!!)
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
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(context.appComponent)
        )[DishDialogViewModel::class.java]
        viewModel.dish = dish
        viewModel.setOrderInfo(tableId, guestCount)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.alertDialogStyle)
        initBinding()
        builder.setView(binding!!.root)
        viewModel.state.observeForever(observer)
        return builder.create()
    }

    private fun initBinding() {
        binding = DialogDishBinding.inflate(layoutInflater)
        binding?.viewModel = viewModel
        initViews(binding!!)
    }

    fun setOrderInfo(tableId: Int, guestCount: Int) {
        this.tableId = tableId
        this.guestCount = guestCount
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.state.removeObserver(observer)
    }

    private fun initViews(binding: DialogDishBinding) {
        binding.dishName.text = dish?.name
        binding.countNumberPicker.maxValue = maxCountValue
        binding.countNumberPicker.minValue = minCountValue
    }

    override fun getTheme(): Int {
        return R.style.alertDialogStyle
    }
}