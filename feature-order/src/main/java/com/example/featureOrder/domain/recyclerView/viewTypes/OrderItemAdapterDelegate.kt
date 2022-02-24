package com.example.featureOrder.domain.recyclerView.viewTypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.featureOrder.R
import com.example.featureOrder.databinding.LayoutOrderItemBinding
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.order.OrderType
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.waiterCore.domain.recyclerView.interfaces.BaseViewHolder
import com.example.waiterCore.domain.recyclerView.interfaces.BaseAdapterDelegate
import javax.inject.Inject

class OrderItemAdapterDelegate @Inject constructor()
    : BaseAdapterDelegate<LayoutOrderItemBinding, OrderType> {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is OrderType

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutOrderItemBinding, OrderType> =
        OrderItemViewHolder(
            MenuService,
            LayoutOrderItemBinding.inflate(inflater, parent, false)
        )

    override fun getLayoutId(): Int = R.layout.layout_order_item

    private val diffCallback = object : DiffUtil.ItemCallback<OrderType>() {
        override fun areItemsTheSame(oldItem: OrderType, newItem: OrderType): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: OrderType, newItem: OrderType): Boolean =
            oldItem == newItem
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<OrderType> = diffCallback
}

class OrderItemViewHolder(
    private val menuService: MenuService,
    override val binding: LayoutOrderItemBinding,
) : BaseViewHolder<LayoutOrderItemBinding, OrderType>(binding) {

    override fun onBind(item: OrderType) {
        val dish = menuService.getDishById(item.dishId)
            ?: throw IllegalArgumentException("Dish not found. ID: ${item.dishId}")
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

