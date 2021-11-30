package com.example.domo.views.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domo.R
import com.example.domo.databinding.FragmentOrderBinding
import com.example.domo.viewModels.SharedViewModelStates
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.WaiterActivityOrderFragmentSharedViewModel
import com.example.domo.views.dialogs.GuestsCountBottomSheetDialog
import com.example.domo.views.dialogs.MenuBottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import extentions.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderFragment : Fragment() {

    private var tableId = 0

    private val transformDuration = 300L
    private val guestCountDelay = 400L

    private var menuBottomSheetDialog: MenuBottomSheetDialog? = null
    private var guestCountDialog: GuestsCountBottomSheetDialog? = null
    private lateinit var binding: FragmentOrderBinding
    private lateinit var sharedViewModel: WaiterActivityOrderFragmentSharedViewModel
    private val args: OrderFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //TODO: Add a delegate for viewModels
        sharedViewModel = ViewModelProvider(requireActivity(),
            ViewModelFactory(context.appComponent))[WaiterActivityOrderFragmentSharedViewModel::class.java]
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initBinding()
        initDialogs()
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
        guestCountDialog = GuestsCountBottomSheetDialog {
            binding.guestsCount.text = it.toString()
            menuBottomSheetDialog?.setOrderInfo(tableId, it)
        }
    }

    private fun initBinding() {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        tableId = args.tableNumber
        binding.tableNumber.text = tableId.toString()
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

    private fun observeViewModelStates() {
        sharedViewModel.states.observe(viewLifecycleOwner) {
            when (it) {
                is SharedViewModelStates.ShowingMenuDialog -> {
                    if (!menuBottomSheetDialog?.isAdded!!)
                        menuBottomSheetDialog?.show(parentFragmentManager, "")
                }
                else -> { } //DefaultState
            }
        }
    }
}