package com.example.domo.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.domo.databinding.DialogDishBinding

class DishAlertDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogDishBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        return builder.create()
    }
}