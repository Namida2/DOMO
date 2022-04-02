package com.example.featureEmployees.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.dialogs.ProcessAlertDialog
import com.example.core.domain.entities.tools.extensions.createMessageDialog
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.featureEmployees.R
import com.example.featureEmployees.databinding.DialogEmployeeDetailBinding
import com.example.featureEmployees.domain.ViewModelFactory

class EmployeeDetailDialog : DialogFragment() {

    lateinit var employee: Employee
    private lateinit var binding: DialogEmployeeDetailBinding
    private val viewModel by viewModels<EmployeeDetailViewModel> { ViewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
        binding = DialogEmployeeDetailBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        initBinding()
        observeViewModelStates()
        return builder.create()
    }

    private fun initBinding() {
        with(binding) {
            changePermissionButton.setOnClickListener {
                viewModel.setPermission(employee, !employee.permission)
            }
            deleteEmployeeButton.setOnClickListener { viewModel.deleteEmployee(employee) }
            layoutProfile.employeeName.text = employee.name
            layoutProfile.employeeEmail.text = employee.email
            layoutProfile.employeePost.text = employee.post
            if (employee.permission) layoutProfile.permission.text =
                resources.getString(R.string.permissionYes)
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(this) {
            when (it) {
                is EmployeeDetailVMStates.InProcess ->
                    ProcessAlertDialog.showIfNotAdded(parentFragmentManager, "")
                is EmployeeDetailVMStates.OnFailure -> {
                    ProcessAlertDialog.dismiss()
                    requireContext().createMessageDialog(it.errorMessage)
                        ?.show(parentFragmentManager, "")
                    viewModel.resetViewModelSate()
                }
                is EmployeeDetailVMStates.OnSuccess -> {
                    ProcessAlertDialog.onSuccess()
                    this.dismiss()
                    viewModel.resetViewModelSate()
                }
                is EmployeeDetailVMStates.Default -> {
                    if (ProcessAlertDialog.isAdded)
                        ProcessAlertDialog.dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setGravity(Gravity.TOP)
        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params
    }

}