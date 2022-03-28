package com.example.featureOrder.presentation.tables

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.featureOrder.R
import com.example.featureOrder.databinding.FragmentTablesBinding
import com.example.featureOrder.domain.di.OrderDepsStore.deps
import com.example.featureOrder.presentation.recyclerView.adapters.TablesAdapter
import com.example.featureOrder.presentation.recyclerView.itemDecorations.TablesItemDecorations
import com.google.android.material.transition.platform.MaterialElevationScale
import com.google.android.material.transition.platform.MaterialSharedAxis
import kotlin.properties.Delegates

//TODO: Start implementing this module
class TablesFragment : Fragment() {

    companion object {
        const val isReturnedFromOrderFragment = "isReturnedFromOrderFragment"
    }

    private val spanCount = 2
    private var smallMargin by Delegates.notNull<Int>()
    private var largeMargin by Delegates.notNull<Int>()
    private var topMargin by Delegates.notNull<Int>()

    private val viewModel: TablesViewModel by viewModels()
    private lateinit var binding: FragmentTablesBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        topMargin = resources.getDimensionPixelSize(R.dimen.top_tables_margin)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTablesBinding.inflate(inflater)
        initRecyclerView(container)
        observeTablesEvent()
        setTransitions()
        return binding.root
    }

    private fun initRecyclerView(container: ViewGroup?) {
        with(binding.tablesContainerRecyclerView) {
            layoutManager = GridLayoutManager(container?.context, spanCount)
            adapter = TablesAdapter(deps.settings.tablesCount, viewModel::onTableClick)
            addItemDecoration(
                TablesItemDecorations(topMargin, largeMargin, smallMargin)
            )
        }
    }

    private fun observeTablesEvent() {
        viewModel.onTableSelected.observe(viewLifecycleOwner) {
            it.getData()?.let { viewOwner ->
                startOrderFragment(viewOwner.getView(requireContext()))
            }
        }
    }

    private fun startOrderFragment(item: View) {
        val direction = TablesFragmentDirections
            .actionTablesFragmentToOrderFragment(item.tag as Int)
        val fragmentExtras =
            FragmentNavigatorExtras(
                //TODO: Add it to com.example.core.domain.constants
                item to "end",
            )
        findNavController().navigate(direction, fragmentExtras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setTransitions() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            isReturnedFromOrderFragment
        )?.observe(viewLifecycleOwner) {
            if (it)
                exitTransition = MaterialElevationScale(false).apply {
                    duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
                }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.resetState()
    }
}