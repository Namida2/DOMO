package com.example.waiterCore.domain.recyclerView.adapterDelegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.waiterCore.R
import com.example.waiterCore.databinding.LayoutOrderItemBinding
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.order.OrderItem
import com.example.waiterCore.domain.recyclerView.interfaces.BaseAdapterDelegate
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.waiterCore.domain.recyclerView.interfaces.BaseViewHolder
import javax.inject.Inject

class OrderItemAdapterDelegate @Inject constructor(
    private val onOrderSelected: (orderId: Int) -> Unit
): BaseAdapterDelegate<LayoutOrderItemBinding, OrderItem> {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is OrderItem

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutOrderItemBinding, OrderItem> =
        OrderItemViewHolder(
            MenuService,
            LayoutOrderItemBinding.inflate(inflater, parent, false)
        )

    override fun getLayoutId(): Int = R.layout.layout_order_item

    private val diffCallback = object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
            oldItem == newItem
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<OrderItem> = diffCallback
}

class OrderItemViewHolder(
    private val menuService: MenuService,
    override val binding: LayoutOrderItemBinding,
) : BaseViewHolder<LayoutOrderItemBinding, OrderItem>(binding) {

    override fun onBind(item: OrderItem) {
        val dish = menuService.getDishById(item.dishId)
        with(binding) {
            category.text = dish.categoryName
            dishName.text = dish.name
            dishCost.text = dish.cost
            dishWeight.text = dish.weight
            dishCount.text = item.count.toString()
            commentary.text = item.commentary
        }
    }
}
