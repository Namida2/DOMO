package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.domo.R
import com.example.domo.databinding.LayoutOrderItemBinding
import com.example.domo.models.interfaces.MenuHolder
import entities.order.OrderItem
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.BaseViewHolder
import entities.recyclerView.interfaces.MenuRecyclerViewType
import javax.inject.Inject

class OrderItemRecyclerViewType @Inject constructor(
    private val menuService: MenuHolder,
) : MenuRecyclerViewType<LayoutOrderItemBinding, OrderItem> {

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean =
        recyclerViewItem is OrderItem

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutOrderItemBinding, OrderItem> =
        OrderItemViewHolder(
            menuService,
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
    private val menuService: MenuHolder,
    override val binding: LayoutOrderItemBinding,
) : BaseViewHolder<LayoutOrderItemBinding, OrderItem>(binding) {

    override fun onBind(item: OrderItem) {
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

