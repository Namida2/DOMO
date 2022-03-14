package com.example.featureCurrentOrders.presentation.currentOrders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.featureCurrentOrders.R
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersBinding
import com.example.featureCurrentOrders.domain.ViewModelFactory
import com.example.featureCurrentOrders.domain.adapters.OrdersAdapterDelegate
import com.google.android.material.transition.platform.MaterialSharedAxis

class CurrentOrdersFragment : Fragment() {

    private lateinit var binding: FragmentCurrentOrdersBinding
    private val viewModel by viewModels<CurrentOrdersViewModel> { ViewModelFactory }

    private var adapter = BaseRecyclerViewAdapter(
        listOf(
            OrdersAdapterDelegate(::onOrderClick)
        )
    )

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
        }
    }

    //TODO: Accept the same order //STOPPED//
    private fun observeNewOrdersEvent() {
        viewModel.newOrdersEvent.observe(viewLifecycleOwner) {
            val ordersList = it.getData() ?: return@observe
            adapter.submitList(ordersList)
        }
    }

    private fun onOrderClick(orderId: Int) {
        val destination = CurrentOrdersFragmentDirections
            .actionCurrentOrdersFragmentToCurrentOrdersDetailFragment(orderId)
        findNavController().navigate(destination)
    }
}