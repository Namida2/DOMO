package com.example.featureMenuDialog.presentation.dishDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.core.domain.menu.Dish
import com.example.core.domain.tools.enims.AddingDishMods
import com.example.core.domain.tools.extensions.createMessageDialog
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogDishBinding
import com.example.featureMenuDialog.domain.ViewModelFactory

class DishAlertDialog(
    private val mode: AddingDishMods = AddingDishMods.INSERTING
) : DialogFragment() {

    var dish: Dish? = null
    private var count: Int = 1
    private var aldCommentary: String = ""

    private lateinit var binding: DialogDishBinding
    private val viewModel by viewModels<DishDialogViewModel> { ViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.orderItem = dish
        viewModel.addingDishMode = mode
        viewModel.aldCommentary = aldCommentary
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
        initBinding()
        builder.setView(binding!!.root)
        viewModel.state.observe(this) {
            when (it) {
                is DishDialogVMStates.DishAlreadyAdded -> {
                    requireContext().createMessageDialog(it.errorMessage!!)
                        ?.show(parentFragmentManager, "")
                }
                is DishDialogVMStates.DishSuccessfulAdded -> {
                    this.dismiss()
                }
                DishDialogVMStates.Default -> {} //Default state
            }
        }
        return builder.create()
    }

    fun setOrderItemData(count: Int, commentary: String) {
        this.count = count
        this.aldCommentary = commentary
    }

    private fun initBinding() {
        binding = DialogDishBinding.inflate(layoutInflater)
        binding?.viewModel = viewModel
        initView(binding!!)
    }

    private fun initView(binding: DialogDishBinding) {
        binding.dishName.text = dish?.name
        binding.dishesCount.setText(count.toString())
        binding.commentary.setText(aldCommentary)
    }

}