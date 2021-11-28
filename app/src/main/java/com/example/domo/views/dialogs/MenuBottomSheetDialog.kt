package com.example.domo.views.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.domo.R
import com.example.domo.adapters.MenuItemsAdapter
import com.example.domo.adapters.itemDecorations.MenuItemDecorations
import com.example.domo.databinding.DialogMenuBinding
import com.example.domo.viewModels.MenuDialogStates
import com.example.domo.viewModels.MenuDialogViewModel
import com.example.domo.viewModels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import entities.interfaces.OnDismissListener
import entities.recyclerView.CategoriesMenuRecyclerViewType
import entities.recyclerView.CategoryLargeRecyclerViewType
import entities.recyclerView.DishRecyclerViewType
import extentions.appComponent

class MenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener,
) : BottomSheetDialogFragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null

    private lateinit var binding: DialogMenuBinding
    private lateinit var viewModel: MenuDialogViewModel
    private var dishDialog = DishAlertDialog()
    private var menuAdapter: MenuItemsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(context.appComponent)).get(
                MenuDialogViewModel::class.java)
        menuAdapter = MenuItemsAdapter(
            listOf(
                CategoriesMenuRecyclerViewType(),
                CategoryLargeRecyclerViewType(),
                DishRecyclerViewType(viewModel::onDishClick)
            )
        )
        binding = DialogMenuBinding.inflate(layoutInflater)
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        observeViewModelStates()
        observeDishEvent()
        return binding.root
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is MenuDialogStates.MenuExists -> {
                    menuAdapter?.submitList(it.items)
                }
                else -> {} //other things
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.menuRecyclerView) {
            setHasFixedSize(true)
            adapter = menuAdapter
            addItemDecoration(
                MenuItemDecorations(smallMargin!!, largeMargin!!)
            )
        }
    }

    private fun observeDishEvent() {
        viewModel.onDishSelected.observe(viewLifecycleOwner) {
            val dish = it.getData()
            if(dishDialog.isAdded || dish == null) return@observe
            dishDialog.dish = dish
            dishDialog.show(parentFragmentManager, "")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.onDismiss()
    }

}