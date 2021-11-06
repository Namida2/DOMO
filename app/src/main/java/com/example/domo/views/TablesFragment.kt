package com.example.domo.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domo.R
import com.example.domo.adapters.TablesAdapter
import com.example.domo.adapters.itemDecorations.TablesItemDecorations
import com.example.domo.databinding.FragmentTablesBinding
import com.google.android.material.transition.Hold
import tools.Animations

class TablesFragment : Fragment() {

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
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTablesBinding.inflate(inflater)
        with(binding.tablesContainerRecyclerView) {
            layoutManager = GridLayoutManager(container?.context, spanCount).apply { }
            adapter = TablesAdapter(tablesCount)
            addItemDecoration(
                TablesItemDecorations(
                    smallMargin!!,
                    largeMargin!!,
                    topTablesMargin!!
                )
            )
        }
        binding.view.setOnClickListener {
            val fragmentExtras =
                FragmentNavigatorExtras(
                    binding.view to "end",
                )
            findNavController().navigate(
                R.id.action_tablesFragment2_to_orderFragment,
                null,
                navOptions { anim {
                    enter = R.anim.anim_slide_in_left
                } },
                fragmentExtras
            )
        }

        return binding.root
    }

}