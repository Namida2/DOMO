package com.example.featureCurrentOrders.presentation.currentOrdersDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.featureCurrentOrders.R
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersDetailBinding
import com.example.featureCurrentOrders.domain.ViewModelFactory
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.waiterCore.domain.recyclerView.adapterDelegates.OrderItemAdapterDelegate
import com.example.waiterCore.domain.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.waiterCore.domain.tools.constants.EmployeePosts.COOK
import com.example.waiterCore.domain.tools.extensions.logD
import com.google.android.material.transition.platform.MaterialSharedAxis

class CurrentOrdersDetailFragment : Fragment() {

    private val args: CurrentOrdersDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentCurrentOrdersDetailBinding
    private val viewModel by viewModels<CurrentOrderDetailViewModel> { ViewModelFactory }
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            OrderItemAdapterDelegate(::onDishSelected)
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentOrdersDetailBinding.inflate(inflater, container, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        initRecyclerView()
        observeDishesExistEvent()
        viewModel.getDishesByOrderId(args.orderId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun initRecyclerView() {
        with(binding) {
            dishesRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            dishesRecyclerView.adapter = adapter
        }
    }
    private fun observeDishesExistEvent() {
        viewModel.dishesExitEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { listOfDishes ->
                adapter.submitList(listOfDishes)
            }
        }
    }

    private fun onDishSelected(dishId: Int) {
        if (CurrentOrderDepsStore.deps.currentEmployee!!.post != COOK) return
        logD("I'm cook.")
    }
}