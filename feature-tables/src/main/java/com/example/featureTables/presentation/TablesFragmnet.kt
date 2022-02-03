package com.example.featureTables.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.featureTables.R
import com.example.featureTables.databinding.FragmentTablesBinding
import com.example.featureTables.domain.TablesAdapter
import com.example.featureTables.domain.TablesItemDecorations
import com.google.android.material.transition.MaterialElevationScale

//TODO: Start implementing this module
class TablesFragment : Fragment() {

    private val viewModel: TablesViewModel by viewModels()

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null
    private var topTablesMargin: Int? = null

    private val spanCount = 2
    private val tablesCount = 28
    private lateinit var binding: FragmentTablesBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        topTablesMargin = resources.getDimensionPixelSize(R.dimen.top_tables_margin)

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
        return binding.root
    }

    private fun initRecyclerView(container: ViewGroup?) {
        with(binding.tablesContainerRecyclerView) {
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300
            }
            layoutManager = GridLayoutManager(container?.context, spanCount)
            adapter = TablesAdapter(tablesCount, viewModel::onTableClick)
            addItemDecoration(
                TablesItemDecorations(
                    smallMargin!!,
                    largeMargin!!,
                    topTablesMargin!!
                )
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
//        val direction = TablesFragmentDirections
//            .actionTablesFragmentToOrderFragment(item.tag as Int)
//        val fragmentExtras =
//            FragmentNavigatorExtras(
//                //TODO: Add it to com.example.core.domain.constants
//                item to "end",
//            )
//        findNavController().navigate(direction, fragmentExtras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.resetState()
    }
}