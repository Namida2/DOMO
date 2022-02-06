package com.example.featureOrder.presentation.order.doalogs

import android.content.Context
import com.example.featureOrder.R
import com.example.featureOrder.databinding.DialogMenuBinding
import com.example.featureOrder.domain.interfaces.OnDismissListener
import com.example.featureOrder.domain.recyclerView.adapters.MenuItemsAdapter
import com.example.featureOrder.presentation.order.doalogs.viewModels.MenuDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetDialog(
    private val onDismissListener: OnDismissListener,
) : BottomSheetDialogFragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null

    private lateinit var binding: DialogMenuBinding
    private lateinit var viewModel: MenuDialogViewModel //by viewModels { ViewModelFactory }
    private var dishDialog = DishAlertDialog()
    private var menuAdapter: MenuItemsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
//        menuAdapter = MenuItemsAdapter(
//            listOf(
//                CategoriesMenuRecyclerViewType(),
//                CategoryLargeRecyclerViewType(),
//                DishRecyclerViewType(viewModel::onDishClick)
//            )
//        )
        binding = DialogMenuBinding.inflate(layoutInflater)
//        initRecyclerView()
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        observeViewModelStates()
//        observeDishEvent()
//        return binding.root
//    }
//
//    private fun observeViewModelStates() {
//        viewModel.state.observe(viewLifecycleOwner) {
//            when (it) {
//                is MenuDialogStates.MenuExists -> {
//                    menuAdapter?.submitList(it.items)
//                }
//                else -> {} //other things
//            }
//        }
//    }
//
//    private fun initRecyclerView() {
//        with(binding.menuRecyclerView) {
//            setHasFixedSize(true)
//            adapter = menuAdapter
//            addItemDecoration(
//                MenuItemDecorations(smallMargin!!, largeMargin!!)
//            )
//        }
//    }
//
//    private fun observeDishEvent() {
//        viewModel.onDishSelected.observe(viewLifecycleOwner) {
//            val dish = it.getData()
//            if(dishDialog.isAdded || dish == null) return@observe
//            dishDialog.dish = dish
//            dishDialog.show(parentFragmentManager, "")
//        }
//    }
//
//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        onDismissListener.onDismiss()
//    }

}