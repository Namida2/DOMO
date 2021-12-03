package com.example.domo.views.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.R
import com.example.domo.adapters.MenuItemsAdapter
import com.example.domo.databinding.FragmentOrderBinding
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.shared.SharedViewModelStates
import com.example.domo.viewModels.shared.WaiterActOrderFragSharedViewModel
import com.example.domo.views.dialogs.GuestsCountBottomSheetDialog
import com.example.domo.views.dialogs.MenuBottomSheetDialog
import com.example.domo.views.dialogs.OrderMenuBottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import entities.recyclerView.OrderItemRecyclerViewType
import extentions.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderFragment : Fragment() {

    private var tableId = 0

    private val transformDuration = 300L
    private val guestCountDelay = 400L

    private var menuBottomSheetDialog: MenuBottomSheetDialog? = null
    private var guestCountDialog: GuestsCountBottomSheetDialog? = null
    private var orderMenuDialog: OrderMenuBottomSheetDialog? = null

    private lateinit var binding: FragmentOrderBinding
    private lateinit var sharedViewModel: WaiterActOrderFragSharedViewModel
    private val args: OrderFragmentArgs by navArgs()

    @Inject
    lateinit var orderItemRecyclerViewType: OrderItemRecyclerViewType
    private var adapter: MenuItemsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //TODO: Add a delegate for viewModels
        sharedViewModel = ViewModelProvider(requireActivity(),
            ViewModelFactory(context.appComponent))[WaiterActOrderFragSharedViewModel::class.java]
        context.appComponent.inject(this)
        adapter = MenuItemsAdapter(
            listOf(orderItemRecyclerViewType)
        )
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initBinding()
        initDialogs()
        observeCurrentOrderChanges()
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = transformDuration
        }
        postponeEnterTransition()
        observeViewModelStates()
        return binding.root
    }

    private fun initDialogs() {
        menuBottomSheetDialog = MenuBottomSheetDialog(sharedViewModel)
        orderMenuDialog = OrderMenuBottomSheetDialog(sharedViewModel)
        guestCountDialog = GuestsCountBottomSheetDialog {
            binding.guestsCount.text = it.toString()
            sharedViewModel.initCurrentOrder(tableId, it)
            adapter?.submitList(sharedViewModel.getCurrentOrderItems().toList())
        }
    }

    private fun initBinding() {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        tableId = args.tableNumber
        with(binding) {
            tableNumber.text = tableId.toString()
            orderRecyclerView.adapter = adapter
            orderRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            orderRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnPreDraw {
            startPostponedEnterTransition()
            showGuestCountDialog()
        }
    }

    private fun showGuestCountDialog() {
        CoroutineScope(Default).launch {
            delay(guestCountDelay)
            withContext(Main) {
                guestCountDialog?.show(parentFragmentManager, "")
            }
        }
    }

    private fun observeCurrentOrderChanges() {
        sharedViewModel.currentOrderChangedEvent.observe(viewLifecycleOwner) {
            val data = it.getData() ?: return@observe
            adapter?.submitList(data)
        }
    }

    private fun observeViewModelStates() {
        sharedViewModel.states.observe(viewLifecycleOwner) {
            when (it) {
                is SharedViewModelStates.ShowingMenuDialog -> {
                    if (!menuBottomSheetDialog?.isAdded!!)
                        menuBottomSheetDialog?.show(parentFragmentManager, "")
                }
                is SharedViewModelStates.ShowingOrderMenuDialog -> {
                    if (!orderMenuDialog?.isAdded!!)
                    orderMenuDialog?.show(parentFragmentManager, "")
                }
                else -> {} //DefaultState
            }
        }
    }
}