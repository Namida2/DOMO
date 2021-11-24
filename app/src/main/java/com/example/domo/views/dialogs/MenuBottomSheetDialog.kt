package com.example.domo.views.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.adapters.MenuItemsAdapter
import com.example.domo.databinding.DialogMenuBinding
import com.example.domo.viewModels.MenuDialogStates
import com.example.domo.viewModels.MenuDialogViewModel
import com.example.domo.viewModels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import entities.interfaces.OnDismissListener
import entities.recyclerView.CategoriesMenuRecyclerViewType
import entities.recyclerView.CategoryLargeRecyclerViewType
import entities.recyclerView.DishMenuRecyclerViewType
import extentions.appComponent

class MenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogMenuBinding
    private lateinit var viewModel: MenuDialogViewModel
    private var menuAdapter = MenuItemsAdapter(
        listOf(
            CategoriesMenuRecyclerViewType(),
            CategoryLargeRecyclerViewType(),
            DishMenuRecyclerViewType()
        )
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelProvider(requireActivity(), ViewModelFactory(context.appComponent)).get(
                MenuDialogViewModel::class.java)
        binding = DialogMenuBinding.inflate(layoutInflater)
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        observeViewModelStates()
        return binding.root
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is MenuDialogStates.MenuExists -> {
                    menuAdapter.submitList(it.items)
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.menuRecyclerView) {
            setHasFixedSize(true)
            adapter = menuAdapter
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.onDismiss()
    }
}