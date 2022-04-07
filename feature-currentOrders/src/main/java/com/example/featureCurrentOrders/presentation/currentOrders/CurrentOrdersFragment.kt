package com.example.featureCurrentOrders.presentation.currentOrders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.tools.constants.EmployeePosts
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.core.presentation.recyclerView.itemDecorations.SimpleListItemDecoration
import com.example.featureCurrentOrders.R
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersBinding
import com.example.featureCurrentOrders.domain.ViewModelFactory
import com.example.featureCurrentOrders.domain.adapters.OrdersAdapterDelegate
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore.deps
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore.onShowOrderDetailCallback
import com.example.featureCurrentOrders.presentation.completedOrderMenu.CompletedOrderDialogCallback
import com.example.featureCurrentOrders.presentation.completedOrderMenu.CompletedOrderMenuDialog
import com.google.android.material.transition.platform.MaterialSharedAxis
import kotlin.properties.Delegates

class CurrentOrdersFragment : Fragment() {

    private var smallMargin by Delegates.notNull<Int>()
    private var largeMargin by Delegates.notNull<Int>()
    private var topMargin by Delegates.notNull<Int>()
    private lateinit var binding: FragmentCurrentOrdersBinding
    private val viewModel by viewModels<CurrentOrdersViewModel> { ViewModelFactory }
    private lateinit var adapter: BaseRecyclerViewAdapter
    private lateinit var completedOrderDialog: CompletedOrderMenuDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        topMargin = resources.getDimensionPixelSize(R.dimen.top_tables_margin)
        completedOrderDialog = CompletedOrderMenuDialog(object : CompletedOrderDialogCallback() {
            override fun showDetail(order: Order) {
                showDetail(order.orderId)
            }
        })
        adapter = BaseRecyclerViewAdapter(
            listOf(OrdersAdapterDelegate(viewModel::onOrderClick))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentOrdersBinding.inflate(inflater, container, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        initRecyclerView()
        observeNewOrdersEvent()
        observeOnOrderSelectedEvent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun initRecyclerView() {
        with(binding) {
            currentOrdersRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            currentOrdersRecyclerView.adapter = adapter
            currentOrdersRecyclerView.addItemDecoration(
                SimpleListItemDecoration(
                    topMargin,
                    largeMargin,
                    smallMargin
                )
            )
        }
    }

    //TODO: Accept the same order //STOPPED//
    private fun observeNewOrdersEvent() {
        viewModel.newOrdersEvent.observe(viewLifecycleOwner) {
            val ordersList = it.getData() ?: return@observe
            adapter.submitList(ordersList)
        }
    }

    private fun observeOnOrderSelectedEvent() {
        viewModel.onOrderSelectedEvent.observe(viewLifecycleOwner) {
            val orderInfo = it.getData() ?: return@observe
            if (orderInfo.isCompleted && deps.currentEmployee?.post == EmployeePosts.WAITER.value) {
                if (!completedOrderDialog.isAdded) {
                    completedOrderDialog.order = orderInfo.order
                    completedOrderDialog.showIfNotAdded(parentFragmentManager, "")
                }
            } else showDetail(orderInfo.order.orderId)
        }
    }

    private fun showDetail(orderId: Int) {
        onShowOrderDetailCallback.onShowDetail(orderId)
        val destination = CurrentOrdersFragmentDirections
            .actionCurrentOrdersFragmentToCurrentOrdersDetailFragment(orderId)
        findNavController().navigate(destination)
    }
}