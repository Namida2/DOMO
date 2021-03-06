package com.example.featureOrder.presentation.order

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.enums.AddingDishMods
import com.example.core.domain.entities.tools.extensions.Animations.prepareSlideUpFromBottom
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.domain.interfaces.OrdersService
import com.example.core.presentation.recyclerView.adapterDelegates.OrderItemsAdapterDelegate
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.core.presentation.recyclerView.itemDecorations.SimpleListItemDecoration
import com.example.featureMenuDialog.domain.MenuDialogDeps
import com.example.featureMenuDialog.domain.MenuDialogDepsStore
import com.example.featureMenuDialog.presentation.dishDialog.DishAlertDialog
import com.example.featureMenuDialog.presentation.menuDialog.MenuBottomSheetDialog
import com.example.featureOrder.R
import com.example.featureOrder.databinding.FragmentOrderBinding
import com.example.featureOrder.domain.ViewModelFactory
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.presentation.order.doalogs.guestsCountDialog.GuestsCountBottomSheetDialog
import com.example.featureOrder.presentation.order.doalogs.orderMenuDialog.OrderMenuBottomSheetDialog
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class OrderFragment : Fragment() {

    private var orderId = 0
    private val defaultGuestsCount = 1
    private val guestCountShowingDelay = 400L
    private var smallMargin by Delegates.notNull<Int>()
    private var largeMargin by Delegates.notNull<Int>()

    private var guestCountDialog: GuestsCountBottomSheetDialog? = null
    private var orderMenuDialog: OrderMenuBottomSheetDialog? = null
    private var dishDialog = DishAlertDialog(AddingDishMods.UPDATING)

    private lateinit var binding: FragmentOrderBinding
    private lateinit var itemDecoration: SimpleListItemDecoration
    private val viewModel by viewModels<OrderViewModel> { ViewModelFactory }
    private val args: OrderFragmentArgs by navArgs()

    private var adapter: BaseRecyclerViewAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.top_tables_margin)
        itemDecoration = SimpleListItemDecoration(largeMargin, smallMargin, smallMargin)
        //TODO: Add a delegate for viewModels
        adapter = BaseRecyclerViewAdapter(
            listOf(
                OrderItemsAdapterDelegate(
                    viewModel::onOrderItemSelected
                )
            )
        )
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        initBinding()
        initDialogs()
        provideMenuDialogDeps()
        observeCurrentOrderChangesEvent()
        postponeEnterTransition()
        observeViewModelStates()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnPreDraw {
            startPostponedEnterTransition()
            CoroutineScope(Main).launch {
                delay(150)
                with(binding) {
                    bottomAppBar.prepareSlideUpFromBottom(bottomAppBar.height).apply {
                        doOnEnd { menuFba.show() }
                    }.start()
                    bottomAppBar.alpha = 1f
                    menuFba.alpha = 1f
                }
            }
            showGuestCountDialog()
        }
    }

    private fun initBinding() {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        orderId = args.orderId
        with(binding) {
            tableNumber.text = orderId.toString()
            toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
            intiOrderData()
        }
    }

    private fun initDialogs() {
        orderMenuDialog = OrderMenuBottomSheetDialog(viewModel) {
            guestCountDialog?.show(parentFragmentManager, "")
        }
        guestCountDialog = GuestsCountBottomSheetDialog {
            binding.guestsCount.text = it.toString()
            viewModel.changeGuestsCount(it)
        }
    }

    private fun provideMenuDialogDeps() {
        MenuDialogDepsStore.deps = object : MenuDialogDeps {
            override val currentEmployee: Employee?
                get() = OrderDepsStore.deps.currentEmployee
            override val ordersService: OrdersService
                get() = OrderDepsStore.deps.ordersService
        }
    }

    private fun intiOrderData() {
        with(binding) {
            orderRecyclerView.adapter = adapter
            this@OrderFragment.viewModel.initCurrentOrder(orderId, defaultGuestsCount)
            val currentOrder = this@OrderFragment.viewModel.getCurrentOrder()
            binding.guestsCount.text = currentOrder.guestsCount.toString()
            binding.orderRecyclerView.addItemDecoration(itemDecoration)
            adapter?.submitList(currentOrder.orderItems.toList())
            orderRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showGuestCountDialog() {
        CoroutineScope(Default).launch {
            delay(guestCountShowingDelay)
            withContext(Main) {
                guestCountDialog?.show(parentFragmentManager, "")
            }
        }
    }

    private fun observeCurrentOrderChangesEvent() {
        viewModel.currentOrderChangedEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { newList ->
                adapter?.submitList(newList)
                binding.bottomAppBar.performShow()
            }
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is OrderViewModelStates.ShowingMenuDialog -> {
                    MenuBottomSheetDialog(viewModel).show(parentFragmentManager, "")
                }
                is OrderViewModelStates.ShowingOrderMenuDialog -> {
                    orderMenuDialog?.showIfNotAdded(parentFragmentManager, "")
                }
                is OrderViewModelStates.ShowingDishMenuDialog -> {
                    dishDialog.dish = it.dish
                    dishDialog.orderItem = it.orderItem
                    dishDialog.setOrderItemData(
                        it.orderItem.count,
                        it.orderItem.commentary
                    )
                    dishDialog.show(parentFragmentManager, "")
                }
                OrderViewModelStates.Default -> {}
            }
        }
    }

}