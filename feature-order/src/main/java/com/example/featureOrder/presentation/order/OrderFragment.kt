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
import androidx.recyclerview.widget.RecyclerView
import com.example.featureOrder.databinding.FragmentOrderBinding
import com.example.featureOrder.domain.ViewModelFactory
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.domain.recyclerView.adapters.MenuItemsAdapter
import com.example.featureOrder.domain.recyclerView.viewTypes.OrderItemRecyclerViewType
import com.example.featureOrder.presentation.order.doalogs.guestsCountDialog.GuestsCountBottomSheetDialog
import com.example.featureOrder.presentation.order.doalogs.menuDialog.MenuBottomSheetDialog
import com.example.featureOrder.presentation.order.doalogs.orderMenuDialog.OrderMenuBottomSheetDialog
import com.example.waiterCore.domain.tools.extensions.Animations.prepareShow
import com.example.waiterCore.domain.tools.extensions.Animations.prepareSlideUp
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderFragment : Fragment() {

    private var tableId = 0
    private val defaultGuestsCount = 1

    private val transformDuration = 300L
    private val guestCountDelay = 400L

    private var menuBottomSheetDialog: MenuBottomSheetDialog? = null
    private var guestCountDialog: GuestsCountBottomSheetDialog? = null
    private var orderMenuDialog: OrderMenuBottomSheetDialog? = null

    private lateinit var binding: FragmentOrderBinding
    private val viewModel by viewModels<OrderViewModel> { ViewModelFactory }
    private val args: OrderFragmentArgs by navArgs()

    @Inject
    lateinit var orderItemRecyclerViewType: OrderItemRecyclerViewType
    private var adapter: MenuItemsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //TODO: Add a delegate for viewModels
        OrderDepsStore.appComponent.inject(this)
        adapter = MenuItemsAdapter(
            listOf(orderItemRecyclerViewType)
        )
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
//            drawingViewId = R.id.nav_host_fragment
            duration = transformDuration
        }
        initBinding()
        initDialogs()
        observeCurrentOrderChanges()
        postponeEnterTransition()
        observeViewModelStates()
        return binding.root
    }

    private fun initDialogs() {
        menuBottomSheetDialog = MenuBottomSheetDialog(viewModel)
        orderMenuDialog = OrderMenuBottomSheetDialog(viewModel)
        guestCountDialog = GuestsCountBottomSheetDialog {
            binding.guestsCount.text = it.toString()
            viewModel.changeGuestsCount(it)
        }
    }

    private fun initBinding() {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        tableId = args.tableId
        with(binding) {
            tableNumber.text = tableId.toString()
            intiOrderData()
        }
    }

    private fun intiOrderData() {
        with(binding) {
            orderRecyclerView.adapter = adapter
            this@OrderFragment.viewModel.initCurrentOrder(tableId, defaultGuestsCount)

            val currentOrder = this@OrderFragment.viewModel.getCurrentOrder()
            binding.guestsCount.text = currentOrder.guestsCount.toString()
            adapter?.submitList(currentOrder.orderItems.toList())

            orderRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            orderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnPreDraw {
            startPostponedEnterTransition()
            CoroutineScope(Main).launch {
                delay(150)
                with(binding) {
                    bottomAppBar.prepareSlideUp(bottomAppBar.height).apply {
                        doOnEnd { menuFba.show() }
                    }.start()
                    bottomAppBar.alpha = 1f
                    menuFba.alpha = 1f
                }
            }
            showGuestCountDialog()
        }
    }

    private fun showGuestCountDialog() {
        CoroutineScope(Default).launch {
            delay(guestCountDelay)
            withContext(Main) {
//                guestCountDialog?.show(parentFragmentManager, "")
            }
        }
    }

    private fun observeCurrentOrderChanges() {
        viewModel.currentOrderChangedEvent.observe(viewLifecycleOwner) {
            val data = it.getData() ?: return@observe
            adapter?.submitList(data)
        }
    }

    private fun observeViewModelStates() {
        viewModel.states.observe(viewLifecycleOwner) {
            when (it) {
                is OrderViewModelStates.ShowingMenuDialog -> {
                    if (!menuBottomSheetDialog?.isAdded!!)
                        menuBottomSheetDialog?.show(parentFragmentManager, "")
                }
                is OrderViewModelStates.ShowingOrderMenuDialog -> {
                    if (!orderMenuDialog?.isAdded!!)
                        orderMenuDialog?.show(parentFragmentManager, "")
                }
                else -> {
                } //DefaultState
            }
        }
    }

}