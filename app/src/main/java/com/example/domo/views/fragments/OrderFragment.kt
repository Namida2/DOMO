package com.example.domo.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.domo.R
import com.google.android.material.transition.MaterialContainerTransform

class OrderFragment : Fragment() {

//    private var tableId = 0
//    private val defaultGuestsCount = 1
//
//    private val transformDuration = 300L
//    private val guestCountDelay = 400L
//
//    private var menuBottomSheetDialog: MenuBottomSheetDialog? = null
//    private var guestCountDialog: GuestsCountBottomSheetDialog? = null
//    private var orderMenuDialog: OrderMenuBottomSheetDialog? = null
//
//    private lateinit var binding: FragmentOrderBinding
//    private val sharedViewModel: WaiterActOrderFragSharedViewModel by activityViewModels { ViewModelFactory }
//    private val args: OrderFragmentArgs by navArgs()
//
//    @Inject
//    lateinit var orderItemRecyclerViewType: OrderItemRecyclerViewType
//    private var adapter: MenuItemsAdapter? = null
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        //TODO: Add a delegate for viewModels
//        context.appComponent.inject(this)
//        adapter = MenuItemsAdapter(
//            listOf(orderItemRecyclerViewType)
//        )
//    }
//
//    @SuppressLint("ResourceType")
//    override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?,
//    ): View? {
//        sharedElementEnterTransition = MaterialContainerTransform().apply {
////            drawingViewId = R.id.nav_host_fragment
//            duration = transformDuration
//        }
//        initBinding()
//        initDialogs()
//        observeCurrentOrderChanges()
//        postponeEnterTransition()
//        observeViewModelStates()
//        return binding.root
//    }
//
//    private fun initDialogs() {
//        menuBottomSheetDialog = MenuBottomSheetDialog(sharedViewModel)
//        orderMenuDialog = OrderMenuBottomSheetDialog(sharedViewModel)
//        guestCountDialog = GuestsCountBottomSheetDialog {
//            binding.guestsCount.text = it.toString()
//            sharedViewModel.changeGuestsCount(it)
//        }
//    }
//
//    private fun initBinding() {
//        binding = FragmentOrderBinding.inflate(layoutInflater)
//        tableId = args.tableNumber
//        with(binding) {
//            tableNumber.text = tableId.toString()
//            intiOrderData()
//        }
//    }
//
//    private fun intiOrderData() {
//        with(binding) {
//            orderRecyclerView.adapter = adapter
//            sharedViewModel.initCurrentOrder(tableId, defaultGuestsCount)
//
//            val currentOrder = sharedViewModel.getCurrentOrder()
//            binding.guestsCount.text = currentOrder.guestsCount.toString()
//            adapter?.submitList(currentOrder.orderItems.toList())
//
//            orderRecyclerView.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            orderRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                }
//            })
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        view.doOnPreDraw {
//            startPostponedEnterTransition()
//            showGuestCountDialog()
//        }
//    }
//
//    private fun showGuestCountDialog() {
//        CoroutineScope(Default).launch {
//            delay(guestCountDelay)
//            withContext(Main) {
//                guestCountDialog?.show(parentFragmentManager, "")
//            }
//        }
//    }
//
//    private fun observeCurrentOrderChanges() {
//        sharedViewModel.currentOrderChangedEvent.observe(viewLifecycleOwner) {
//            val data = it.getData() ?: return@observe
//            adapter?.submitList(data)
//        }
//    }
//
//    private fun observeViewModelStates() {
//        sharedViewModel.states.observe(viewLifecycleOwner) {
//            when (it) {
//                is SharedViewModelStates.ShowingMenuDialog -> {
//                    if (!menuBottomSheetDialog?.isAdded!!)
//                        menuBottomSheetDialog?.show(parentFragmentManager, "")
//                }
//                is SharedViewModelStates.ShowingOrderMenuDialog -> {
//                    if (!orderMenuDialog?.isAdded!!)
//                        orderMenuDialog?.show(parentFragmentManager, "")
//                }
//                else -> {} //DefaultState
//            }
//        }
//    }
}