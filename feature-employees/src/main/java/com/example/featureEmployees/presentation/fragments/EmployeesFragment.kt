package com.example.featureEmployees.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.core.presentation.recyclerView.itemDecorations.SimpleListItemDecoration
import com.example.featureEmployees.R
import com.example.featureEmployees.databinding.FragmentEmployeesBinding
import com.example.featureEmployees.domain.ViewModelFactory
import com.example.featureEmployees.presentation.dialogs.EmployeeDetailDialog
import com.example.featureEmployees.presentation.recyclerView.EmployeesAdapterDelegate
import com.google.android.material.transition.platform.MaterialSharedAxis
import kotlin.properties.Delegates

class EmployeesFragment: Fragment() {

    private var smallMargin by Delegates.notNull<Int>()
    private var largeMargin by Delegates.notNull<Int>()
    private var topMargin by Delegates.notNull<Int>()
    private lateinit var binding: FragmentEmployeesBinding
    private val viewModel by viewModels<EmployeesViewModel> { ViewModelFactory }
    private val employeeDetailDialog: EmployeeDetailDialog = EmployeeDetailDialog()
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            EmployeesAdapterDelegate(::onEmployeeSelected)
        )
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        topMargin = resources.getDimensionPixelSize(R.dimen.top_tables_margin)
        viewModel.readEmployees()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.transitionAnimationDuration).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false)
        observeViewModelStates()
        observeNewEmployeesEvent()
        initBinding()
        return binding.root
    }

    private fun initBinding() {
        with(binding) {
            employeesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            employeesRecyclerView.adapter = adapter
            employeesRecyclerView.addItemDecoration(SimpleListItemDecoration(
                topMargin, largeMargin, smallMargin
            ))
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is EmployeesVMStates.ReadingData -> {}
                is EmployeesVMStates.ReadingFailed -> {}
                is EmployeesVMStates.ReadingWasSuccessful -> {}
                is EmployeesVMStates.Default -> {}
            }
        }
    }

    private fun observeNewEmployeesEvent() {
        viewModel.newEmployeesEvent.observe(viewLifecycleOwner) { event ->
            event.getData()?.let { adapter.submitList(it) }
        }
    }

    private fun onEmployeeSelected(employee: Employee) {
        employeeDetailDialog.employee = employee
        employeeDetailDialog.showIfNotAdded(parentFragmentManager, "")
    }
}