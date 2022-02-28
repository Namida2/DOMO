package com.example.featureCurrentOrders.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.order.Order
import com.example.core.domain.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.domain.recyclerView.interfaces.BaseViewHolder
import com.example.featureCurrentOrders.R
import com.example.featureCurrentOrders.databinding.LayoutOrderCardBinding

//TODO: Implement this part //STOPPED//
class OrdersAdapterDelegate(
    private val onOrderSelected: (tableId: Int) -> Unit
) : BaseAdapterDelegate<LayoutOrderCardBinding, Order>, View.OnClickListener {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean = recyclerViewType is Order
    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = newItem == newItem
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            newItem == newItem
    }

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<LayoutOrderCardBinding, Order> {
        val binding = LayoutOrderCardBinding.inflate(inflater, parent, false).also {
            it.largeOrderContainer.setOnClickListener(this)
            it.orderContainerCardView.setOnClickListener(this)
        }
        return OrdersViewHolder(binding)
    }

    override fun getLayoutId(): Int = R.layout.fragment_current_orders_detail

    override fun getDiffCallback(): DiffUtil.ItemCallback<Order> = diffCallback
    override fun onClick(v: View?) {
        v?.tag?.let { onOrderSelected.invoke(it as Int) }
    }
}

class OrdersViewHolder(
    override val binding: LayoutOrderCardBinding
) : BaseViewHolder<LayoutOrderCardBinding, Order>(binding) {

    override fun onBind(item: Order) {
        binding.largeOrderContainer.tag = item.orderId
        binding.orderContainerCardView.tag = item.orderId
        binding.orderId.text = item.orderId.toString()
        binding.orderId.text = item.orderId.toString()
    }
}