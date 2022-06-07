package com.example.featureCurrentOrders.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.tools.extensions.precomputeAndSetText
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder
import com.example.featureCurrentOrders.R
import com.example.featureCurrentOrders.databinding.LayoutOrderCardBinding

class OrdersAdapterDelegate(
    private val onOrderSelected: (order: Order) -> Unit
) : BaseAdapterDelegate<LayoutOrderCardBinding, Order>, View.OnClickListener {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean = recyclerViewType is Order
    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
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
        v?.tag?.let { onOrderSelected.invoke(it as Order) }
    }
}

class OrdersViewHolder(
    override val binding: LayoutOrderCardBinding
) : BaseViewHolder<LayoutOrderCardBinding, Order>(binding) {
    private val tooLongNamePreviewDelimiter = "...\n"
    private val namePreviewDelimiter = "\n"
    private val maxDishNameLength = 16
    private val averageOrderItemsCount = 4
    override fun onBind(item: Order) {
        var preview = ""
        var allReady = true
        binding.largeOrderContainer.tag = item
        binding.orderContainerCardView.tag = item
        binding.guestsCount.text = item.guestsCount.toString()
        binding.orderId.precomputeAndSetText(item.orderId.toString())
        val count = item.orderItems.size.coerceAtMost(averageOrderItemsCount)
        item.orderItems.take(count).forEach {
            val name = MenuService.getDishById(it.dishId)?.name ?: return
            preview += when {
                name.length > maxDishNameLength -> name.substring(0, maxDishNameLength) + tooLongNamePreviewDelimiter
                else -> name + namePreviewDelimiter
            }
            if(!it.isReady) allReady = false
        }
        binding.preview.precomputeAndSetText(preview)
        if(allReady) binding.completeTextView.visibility = View.VISIBLE
        else binding.completeTextView.visibility = View.GONE
    }
}