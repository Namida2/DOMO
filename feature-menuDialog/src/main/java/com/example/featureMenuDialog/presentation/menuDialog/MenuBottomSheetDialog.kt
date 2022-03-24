package com.example.featureMenuDialog.presentation.menuDialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.core.domain.tools.constants.EmployeePosts.ADMINISTRATOR
import com.example.core.domain.tools.extensions.Animations.prepareHide
import com.example.core.domain.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentation.recyclerView.adapterDelegates.DishesAdapterDelegate
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogMenuBinding
import com.example.featureMenuDialog.domain.MenuDialogDepsStore.deps
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureMenuDialog.domain.tools.MenuItemsEnum
import com.example.featureMenuDialog.presentation.dishDialog.DishAlertDialog
import com.example.featureMenuDialog.presentation.editMenuItemDialog.EditMenuItemDialog
import com.example.featureMenuDialog.presentation.recyclerView.ItemTouchCallback
import com.example.featureMenuDialog.presentation.recyclerView.MenuItemDecorations
import com.example.featureMenuDialog.presentation.recyclerView.delegates.CategoriesAdapterDelegate
import com.example.featureMenuDialog.presentation.recyclerView.delegates.CategoryLargeRecyclerViewType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

//TODO: Remove the edit views for waiter
class MenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener?,
) : BottomSheetDialogFragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null

    private var isAdmin = false
    private lateinit var binding: DialogMenuBinding
    private val viewModel by viewModels<MenuDialogViewModel>()
    private var dishDialog = DishAlertDialog()
    private lateinit var menuAdapter: BaseRecyclerViewAdapter
    private lateinit var itemTouchCallback: ItemTouchCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (deps.currentEmployee?.post == ADMINISTRATOR.value) isAdmin = true
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        menuAdapter = BaseRecyclerViewAdapter(
            listOf(
                CategoriesAdapterDelegate(),
                CategoryLargeRecyclerViewType(isAdmin, ::onAddDishButtonClick),
                DishesAdapterDelegate(viewModel::onDishClick)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogMenuBinding.inflate(layoutInflater, container, false)
        itemTouchCallback = ItemTouchCallback(viewModel::onDishDelete)
        initBinding()
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

    private fun initBinding() {
        with(binding) {
            menuRecyclerView.adapter = menuAdapter
            menuRecyclerView.addItemDecoration(
                MenuItemDecorations(smallMargin!!, largeMargin!!)
            )
            if(isAdmin) {
                ItemTouchHelper(itemTouchCallback)
                    .attachToRecyclerView(menuRecyclerView)
            } else {
                fabMenu.visibility = View.GONE
                fba.visibility = View.VISIBLE
            }
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
                hideFabMenu()
                Snackbar.make(binding.fabMenu, R.string.cancelAction, Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes) {
                        viewModel.addDish(deletedDishInfo)
                    }.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()
            }
        }
    }

    private fun hideFabMenu() {
        val scrollBounds = Rect()
        binding.rootView.getHitRect(scrollBounds)
        if (binding.fabMenu.getLocalVisibleRect(scrollBounds))
            binding.fabMenu.prepareSlideDown(binding.fabMenu.height).start()
    }

    private fun onAddDishButtonClick(categoryName: String) {
        EditMenuItemDialog(MenuItemsEnum.CATEGORY).show(parentFragmentManager, "")
    }

}