package com.example.domo.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.service.autofill.ImageTransformation
import android.transition.Fade
import android.transition.TransitionInflater
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.view.MenuHost
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domo.R
import com.example.domo.adapters.TablesAdapter
import com.example.domo.adapters.itemDecorations.TablesItemDecorations
import com.example.domo.databinding.FragmentOrderBinding
import com.example.domo.databinding.FragmentTablesBinding
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.MenuHolderStates
import com.google.android.material.transition.MaterialElevationScale
import extentions.appComponent
import javax.inject.Inject


class TablesFragment : Fragment() {

    private var smallMargin: Int? = null
    private var largeMargin: Int? = null
    private var topTablesMargin: Int? = null

    private val spanCount = 2
    private val tablesCount = 28
    private lateinit var binding: FragmentTablesBinding

    @Inject
    lateinit var menuHolder: MenuHolder

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
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
        initRecyclerView(container)


        return binding.root
    }

    private fun initRecyclerView(container: ViewGroup?) {
        with(binding.tablesContainerRecyclerView) {
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300
            }
            layoutManager = GridLayoutManager(container?.context, spanCount)
            adapter = TablesAdapter(tablesCount) {
                val fragmentExtras =
                    FragmentNavigatorExtras(
                        //TODO: Add it to the constance
                        it!! to "end",
                    )
                findNavController().navigate(
                    R.id.action_tablesFragment2_to_orderFragment,
                    null,
                    null,
                    fragmentExtras
                )
            }
            addItemDecoration(
                TablesItemDecorations(
                    smallMargin!!,
                    largeMargin!!,
                    topTablesMargin!!
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}