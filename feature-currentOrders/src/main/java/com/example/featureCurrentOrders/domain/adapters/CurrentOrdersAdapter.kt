package com.example.featureCurrentOrders.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.featureCurrentOrders.databinding.LayoutOrderCardBinding
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrderItem
import com.example.waiterCore.domain.recyclerView.interfaces.BaseAdapterDelegate
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.waiterCore.domain.recyclerView.interfaces.BaseViewHolder
import javax.inject.Inject

class CurrentOrdersAdapter @Inject constructor(): RecyclerView.Adapter<CurrentOrdersAdapter.ViewHolder>() {
    class ViewHolder(
        val binding: LayoutOrderCardBinding
    ): RecyclerView.ViewHolder(binding.root)

    private var ordersList: List<Order> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutOrderCardBinding.inflate(inflater, parent, false)
        //TODO: Add an onClick listener
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrder = ordersList[position]
        with(holder.binding) {
            tableId.text = currentOrder.orderId.toString()

        }
    }

    override fun getItemCount(): Int = ordersList.size

    fun setOrdersList(ordersList: List<Order>) {
        this.ordersList = ordersList
        //TODO: this.notifyDataSetChanged()
        this.notifyDataSetChanged()
    }
}

//TODO: Implement this part //STOPPED//
class OrdersAdapterDelegate(
    private val onOrderSelected: (tableId: Int) -> Unit
): BaseAdapterDelegate<LayoutOrderCardBinding, Order> {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean = recyclerViewType is Order

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<LayoutOrderCardBinding, Order> {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<Order> {
        TODO("Not yet implemented")
    }
}

class OrdersViewHolder(
    override val binding: LayoutOrderCardBinding
): BaseViewHolder<LayoutOrderCardBinding, Order>(binding) {

    override fun onBind(item: Order) {
        binding.orderId.text = item.orderId.toString()
    }

}