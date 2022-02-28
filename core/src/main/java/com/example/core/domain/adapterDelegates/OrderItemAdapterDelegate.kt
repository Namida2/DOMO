package com.example.core.domain.adapterDelegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.R
import com.example.core.databinding.LayoutOrderItemBinding
import com.example.core.domain.menu.MenuService
import com.example.core.domain.order.OrderItem
import com.example.core.domain.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.domain.recyclerView.interfaces.BaseViewHolder
import javax.inject.Inject

class OrderItemAdapterDelegate @Inject constructor(
    private val onOrderSelected: (orderId: Int) -> Unit
) : BaseAdapterDelegate<LayoutOrderItemBinding, OrderItem>, View.OnClickListener {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is OrderItem

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutOrderItemBinding, OrderItem> {
        val binding = LayoutOrderItemBinding.inflate(inflater, parent, false)
        binding.orderLargeContainer.setOnClickListener(this)
        binding.orderSmallContainer.setOnClickListener(this)
        return OrderItemViewHolder(binding)
    }

    override fun getLayoutId(): Int = R.layout.layout_order_item

    private val diffCallback = object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
            oldItem == newItem
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<OrderItem> = diffCallback

    override fun onClick(v: View?) {
        v?.tag?.let { onOrderSelected.invoke(it as Int) }
    }
}

class OrderItemViewHolder(
    override val binding: LayoutOrderItemBinding,
) : BaseViewHolder<LayoutOrderItemBinding, OrderItem>(binding) {

    override fun onBind(item: OrderItem) {
        val dish = MenuService.getDishById(item.dishId)
        with(binding) {
            binding.orderLargeContainer.tag = dish.id
            binding.orderSmallContainer.tag = dish.id
            category.text = dish.categoryName
            dishName.text = dish.name
            dishCost.text = dish.cost
            dishWeight.text = dish.weight
            dishCount.text = item.count.toString()
            commentary.text = item.commentary + "no commentary"
        }
    }
}

