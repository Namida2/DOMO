package com.example.domo.views

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import application.appComponent
import com.example.domo.R
import com.example.domo.adapters.TablesAdapter
import com.example.domo.adapters.itemDecorations.TablesItemDecorations
import com.example.domo.databinding.FragmentTablesBinding
import com.example.domo.viewModels.RegistrationViewModel
import com.example.domo.viewModels.ViewModelFactory

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTablesBinding.inflate(inflater)
        with(binding.tablesContainerRecyclerView) {
            layoutManager = GridLayoutManager(container?.context, spanCount).apply {  }
            adapter = TablesAdapter(tablesCount)
            addItemDecoration(TablesItemDecorations(
                smallMargin!!,
                largeMargin!!,
                topTablesMargin!!)
            )
        }

        return binding.root
    }
}