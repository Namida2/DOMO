package com.example.featureEmployees.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.featureEmployees.databinding.FragmentEmployeesBinding
import com.example.featureEmployees.domain.ViewModelFactory
import com.example.featureEmployees.presentation.recyclerView.EmployeesAdapterDelegate

class EmployeesFragment : Fragment() {

    private lateinit var binding: FragmentEmployeesBinding
    private val viewModel by viewModels<EmployeesViewModel> { ViewModelFactory }
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            EmployeesAdapterDelegate()
        )
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.readEmployees()
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
        }
    }

    private fun observeViewModelStates() {:
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is EmployeesVMStates.ReadingData -> {

                }
                is EmployeesVMStates.ReadingFailed -> {

                }
                is EmployeesVMStates.EmptyEmployeesList -> {

                }
                is EmployeesVMStates.ReadingWasSuccessful -> {

                }
                is EmployeesVMStates.Default -> {

                }
            }
        }
    }

    private fun observeNewEmployeesEvent() {
        viewModel.newEmployeesEvent.observe(viewLifecycleOwner) { event ->
            event.getData()?.let { adapter.submitList(it) }
        }
    }

}