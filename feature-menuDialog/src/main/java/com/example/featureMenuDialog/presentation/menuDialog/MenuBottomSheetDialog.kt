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
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.entities.tools.dto.DeletedDishInfo
import com.example.core.domain.entities.tools.constants.EmployeePosts.ADMINISTRATOR
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideDown
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideUpFromBottom
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.presentation.recyclerView.adapterDelegates.DishesAdapterDelegate
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.DialogMenuBinding
import com.example.featureMenuDialog.domain.MenuDialogDepsStore.deps
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureMenuDialog.domain.tools.EditMenuDialogModes
import com.example.featureMenuDialog.presentation.addCategoryDialog.AddCategoryDialog
import com.example.featureMenuDialog.presentation.dishDialog.DishAlertDialog
import com.example.featureMenuDialog.presentation.editMenuItemDialog.EditMenuItemDialog
import com.example.featureMenuDialog.presentation.recyclerView.ItemTouchCallback
import com.example.featureMenuDialog.presentation.recyclerView.MenuItemDecorations
import com.example.featureMenuDialog.presentation.recyclerView.delegates.CategoriesAdapterDelegate
import com.example.featureMenuDialog.presentation.recyclerView.delegates.CategoryLargeRecyclerViewType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar

class MenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener?,
    private val onSaveMenuButtonClick: () -> Unit = {}
) : BottomSheetDialogFragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null

    private var isAdmin = false
    private val startPosition = 0
    private lateinit var binding: DialogMenuBinding
    private val viewModel by viewModels<MenuDialogViewModel>()
    private var dishDialog = DishAlertDialog()
    private lateinit var menuAdapter: BaseRecyclerViewAdapter
    private lateinit var itemTouchCallback: ItemTouchCallback

    private lateinit var smoothScroller: RecyclerView.SmoothScroller

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smoothScroller = object : LinearSmoothScroller(requireContext()) {
            override fun getVerticalSnapPreference(): Int = SNAP_TO_START
        }
        if (deps.currentEmployee?.post == ADMINISTRATOR.value) isAdmin = true
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        menuAdapter = BaseRecyclerViewAdapter(
            listOf(
                CategoriesAdapterDelegate(::goToPosition),
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
            if (isAdmin) {
                ItemTouchHelper(itemTouchCallback)
                    .attachToRecyclerView(menuRecyclerView)
                goToTopFab.setOnClickListener { goToPosition(startPosition) }
                createNewCategoryFab.setOnClickListener {
                    AddCategoryDialog().show(parentFragmentManager, "")
                }
                saveMenuFab.setOnClickListener { onSaveMenuButtonClick() }
            } else {
                fabMenu.visibility = View.GONE
                fba.visibility = View.VISIBLE
                fba.setOnClickListener { goToPosition(startPosition) }
            }
        }
    }

    private fun goToPosition(position: Int) {
        smoothScroller.targetPosition = position
        binding.menuRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun observeDishEvent() {
        viewModel.onDishSelected.observe(viewLifecycleOwner) {
            val dish = it.getData() ?: return@observe
            if (isAdmin) {
                EditMenuItemDialog(
                    EditMenuDialogModes.EDIT_DISH, dish
                ).showIfNotAdded(parentFragmentManager, "")
                return@observe
            }
            if (dishDialog.isAdded) return@observe
            dishDialog.dish = dish
            dishDialog.showIfNotAdded(parentFragmentManager, "")
        }
    }

    private fun observeOnDishDeletedEvent() {
        viewModel.onDishDeleted.observe(viewLifecycleOwner) {
            it.getData()?.let { deletedDishInfo ->
                hideFabMenu()
                showCancelActionSnackBar(deletedDishInfo)
            }
        }
    }

    private fun showCancelActionSnackBar(deletedDishInfo: DeletedDishInfo) {
        Snackbar.make(binding.fabMenu, R.string.cancelAction, Snackbar.LENGTH_LONG)
            .setAction(R.string.yes) {
                viewModel.addDish(deletedDishInfo)
            }.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .addCallback(object : BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    showFabMenu()
                }
            }).show()
    }

    private fun hideFabMenu() {
        val scrollBounds = Rect()
        binding.rootView.getHitRect(scrollBounds)
        if (binding.fabMenu.getLocalVisibleRect(scrollBounds))
            binding.fabMenu.prepareSlideDown(binding.fabMenu.height).start()
    }

    private fun showFabMenu() {
        val scrollBounds = Rect()
        binding.rootView.getHitRect(scrollBounds)
        if (!binding.fabMenu.getLocalVisibleRect(scrollBounds))
            binding.fabMenu.prepareSlideUpFromBottom(binding.fabMenu.height).start()
    }

    private fun onAddDishButtonClick(categoryName: String) {
        EditMenuItemDialog(
            EditMenuDialogModes.ADD_DISH,
            categoryName
        ).showIfNotAdded(parentFragmentManager, "")
    }
}