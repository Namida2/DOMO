package com.example.core.presentation.recyclerView.adapterDelegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.R
import com.example.core.databinding.LayoutOrderItemBinding
import com.example.core.domain.menu.MenuService
import com.example.core.domain.order.OrderItem
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder
import com.example.core.domain.tools.constants.FirestoreConstants.ORDER_ITEM_ID_DELIMITER
import com.example.core.domain.tools.constants.FirestoreConstants.EMPTY_COMMENTARY
import javax.inject.Inject

class OrderItemsAdapterDelegate @Inject constructor(
    private val onOrderSelected: (orderItem: OrderItem) -> Unit
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
        v?.tag?.let { onOrderSelected.invoke(it as OrderItem) }
    }
}

class OrderItemViewHolder(
    override val binding: LayoutOrderItemBinding,
) : BaseViewHolder<LayoutOrderItemBinding, OrderItem>(binding) {

    override fun onBind(item: OrderItem) {
        val dish = MenuService.getDishById(item.dishId)
//        val orderItemId = dish.id.toString() + ORDER_ITEM_ID_DELIMITER + item.commentary
        with(binding) {
            orderLargeContainer.tag = item
            orderSmallContainer.tag = item
            categoryName.text = dish.categoryName
            dishName.text = dish.name
            dishCost.text = dish.cost
            dishWeight.text = dish.weight
            dishCount.text = item.count.toString()
            commentary.text = item.commentary
            setConditionalData(item)
        }
    }

    private fun setConditionalData(item: OrderItem) {
        with(binding) {
            if (item.commentary == EMPTY_COMMENTARY) commentary.visibility = View.GONE
            else commentary.visibility = View.VISIBLE
            if (item.isReady) isReady.visibility = View.VISIBLE
            else isReady.visibility = View.GONE
        }

    }
}

