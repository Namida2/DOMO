package com.example.featureMenuDialog.presentation.menuDialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentation.recyclerView.adapterDelegates.DishesAdapterDelegate
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogMenuBinding
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureMenuDialog.presentation.dishDialog.DishAlertDialog
import com.example.featureMenuDialog.presentation.recyclerView.ItemTouchCallback
import com.example.featureMenuDialog.presentation.recyclerView.MenuItemDecorations
import com.example.featureMenuDialog.presentation.recyclerView.delegates.CategoriesAdapterDelegate
import com.example.featureMenuDialog.presentation.recyclerView.delegates.CategoryLargeRecyclerViewType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class MenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener?,
) : BottomSheetDialogFragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null

    private lateinit var binding: DialogMenuBinding
    private val viewModel by viewModels<MenuDialogViewModel>()
    private var dishDialog = DishAlertDialog()
    private lateinit var menuAdapter: BaseRecyclerViewAdapter
    private lateinit var itemTouchCallback: ItemTouchCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        menuAdapter = BaseRecyclerViewAdapter(
            listOf(
                CategoriesAdapterDelegate(),
                CategoryLargeRecyclerViewType(),
                DishesAdapterDelegate(viewModel::onDishClick)
            )
        )
        viewModel.listenMenuChanges()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogMenuBinding.inflate(layoutInflater, container, false)
        itemTouchCallback = ItemTouchCallback(viewModel::onDishDelete)
        initRecyclerView()
        observeDishEvent()
        observeViewModelStates()
        observeOnDishDeletedEvent()
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss()
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is MenuDialogStates.NewMenu -> {
                    val currentList = menuAdapter.currentList
                    if (currentList == it.items)
                        logD("same")
                    menuAdapter.submitList(it.items)
                }
                is MenuDialogStates.Default -> {}
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
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }

    private fun observeDishEvent() {
        viewModel.onDishSelected.observe(viewLifecycleOwner) {
            val dish = it.getData()
            if (dishDialog.isAdded || dish == null) return@observe
            dishDialog.dish = dish
            dishDialog.show(parentFragmentManager, "")
        }
    }

    private fun observeOnDishDeletedEvent() {
        viewModel.onDishDeleted.observe(viewLifecycleOwner) {
            it.getData()?.let { deletedDishInfo ->
                Snackbar.make(binding.fbaMenu, R.string.cancelAction, Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes) {
                        viewModel.addDish(deletedDishInfo)
                    }.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
        }
    }

}