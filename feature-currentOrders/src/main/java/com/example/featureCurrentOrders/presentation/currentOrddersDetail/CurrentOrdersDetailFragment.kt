package com.example.featureCurrentOrders.presentation.currentOrddersDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersBinding
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersDetailBinding
import com.example.featureCurrentOrders.domain.ViewModelFactory
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersViewModel
import com.example.waiterCore.domain.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.waiterCore.domain.recyclerView.viewTypes.DishesAdapterDelegate
import com.example.waiterCore.domain.tools.constants.EmployeePosts.WAITER
import com.example.waiterCore.domain.tools.extensions.logD

class CurrentOrdersDetailFragment : Fragment() {

    private lateinit var binding: FragmentCurrentOrdersDetailBinding
    private val viewModel by viewModels<CurrentOrderDetailViewModel> { ViewModelFactory }
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            DishesAdapterDelegate(::onDishSelected)
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentOrdersDetailBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        with(binding) {
            dishesRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }

    //TODO: Implement this part //SOPPED//
    private fun observeDishesExistEvent() {
        viewModel.appComponent
    }

    private fun onDishSelected(dishId: Int) {
        if (CurrentOrderDepsStore.deps.currentEmployee.post != WAITER) return
        logD("I'm cook.")
    }
}