package com.example.domo.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.domo.R
import com.example.domo.databinding.DialogDishBinding
import entities.menu.Dish

class DishAlertDialog: DialogFragment() {

    private val maxValue = 20
    private val minValue = 1

    var dish: Dish? = null

    private val binding:  DialogDishBinding? = null

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