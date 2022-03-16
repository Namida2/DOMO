package com.example.featureEmployees.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.Employee
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder
import com.example.featureEmployees.R
import com.example.featureEmployees.databinding.LayoutEmployeeCardBinding

class EmployeesAdapterDelegate(
    private val onEmployeeSelected: (employee: Employee) -> Unit
) : BaseAdapterDelegate<LayoutEmployeeCardBinding, Employee>, View.OnClickListener {
    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is Employee

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<LayoutEmployeeCardBinding, Employee> {
        val binding = LayoutEmployeeCardBinding.inflate(inflater, parent, false).also {
            it.largeCard.setOnClickListener(this)
            it.innerCard.setOnClickListener(this)
        }
        return EmployeesViewHolder(binding)
    }

    override fun getLayoutId(): Int = R.layout.layout_employee_card

    private val diffCallback = object : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean =
            oldItem == newItem
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<Employee> = diffCallback

    override fun onClick(v: View?) {
        v?.tag?.let {onEmployeeSelected.invoke(it as Employee)}
    }
}

class EmployeesViewHolder(
    override val binding: LayoutEmployeeCardBinding
) : BaseViewHolder<LayoutEmployeeCardBinding, Employee>(binding) {

    override fun onBind(item: Employee) {
        with(binding) {
            largeCard.tag = item
            innerCard.tag = item
            employeeName.text = item.name
            employeeEmail.text = item.email
            employeePost.text = item.post
            if(item.permission) permission.visibility = View.VISIBLE
            else permission.visibility = View.GONE
        }
    }

}