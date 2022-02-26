package com.example.featureCurrentOrders.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.featureCurrentOrders.databinding.LayoutOrderCardBinding
import com.example.waiterCore.domain.order.Order
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