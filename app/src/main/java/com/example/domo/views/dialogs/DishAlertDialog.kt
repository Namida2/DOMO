package com.example.domo.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.domo.R
import com.example.domo.databinding.DialogDishBinding
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.dialogs.DishDialogViewModel
import entities.menu.Dish
import extentions.appComponent

class DishAlertDialog: DialogFragment() {

    private val maxValue = 20
    private val minValue = 1

    var dish: Dish? = null
    set(value) {
        field = value
        viewModel
    }

    private val binding:  DialogDishBinding? = null
    private lateinit var viewModel: DishDialogViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(context.appComponent)
        )[DishDialogViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogDishBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context, R.style.alertDialogStyle)
        initViews(binding)
        builder.setView(binding.root)
        return builder.create()
    }

    private fun initViews(binding: DialogDishBinding) {
        binding.dishName.text = dish?.name
        binding.countNumberPicker.maxValue = maxValue
        binding.countNumberPicker.minValue = minValue
    }

}