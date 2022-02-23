package com.example.featureCurrentOrders.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.featureCurrentOrders.R
import com.example.featureCurrentOrders.databinding.FragmentCurrentOrdersBinding
import com.example.featureCurrentOrders.domain.ViewModelFactory
import com.example.featureCurrentOrders.domain.adapters.CurrentOrdersAdapter
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.platform.MaterialSharedAxis
import javax.inject.Inject

class CurrentOrdersFragment : Fragment() {

    private lateinit var binding: FragmentCurrentOrdersBinding
    private val viewModel by viewModels<CurrentOrdersViewModel> { ViewModelFactory }

    @Inject
    lateinit var adapter: CurrentOrdersAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.appComponent.inject(this)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun initRecyclerView() {
        with(binding) {
            currentOrdersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            currentOrdersRecyclerView.adapter = adapter
        }
    }

    private fun observeNewOrdersEvent() {
        viewModel.newOrdersEvent.observe(viewLifecycleOwner) {
            val ordersList = it.getData() ?: return@observe
            adapter.setOrdersList(ordersList)
        }
    }



}