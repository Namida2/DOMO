package entities.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.domo.R
import com.example.domo.databinding.LayoutDishBinding
import com.example.waiter_core.domain.menu.Dish
import com.example.waiter_core.domain.recyclerView.interfaces.BaseRecyclerViewItem
import com.example.waiter_core.domain.recyclerView.interfaces.BaseViewHolder
import com.example.waiter_core.domain.recyclerView.interfaces.MenuRecyclerViewType

class DishRecyclerViewType(
    private val onDishSelected: (dishId: Int) -> Unit,
) : MenuRecyclerViewType<LayoutDishBinding, Dish>, View.OnClickListener {

    private val diffCallback = object : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean = oldItem == newItem
    }

    override fun getLayoutId(): Int = R.layout.layout_dish

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean = recyclerViewItem is Dish

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutDishBinding, Dish> {
        val binding = LayoutDishBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return DishViewHolder(binding)
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<Dish> = diffCallback

    override fun onClick(view: View?) {
        onDishSelected.invoke(view?.tag as Int)
    }

}

class DishViewHolder(
    override val binding: LayoutDishBinding,
) : BaseViewHolder<LayoutDishBinding, Dish>(binding) {

    override fun onBind(item: Dish) {
        with(binding) {
            dishName.text = item.name
            dishConst.text = item.cost
            dishWeight.text = item.weight
            root.tag = item.id
        }
    }

}