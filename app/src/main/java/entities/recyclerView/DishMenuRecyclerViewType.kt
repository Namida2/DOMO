package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.domo.R
import entities.recyclerView.interfaces.BaseViewHolder
import com.example.domo.databinding.LayoutDishBinding
import entities.Dish
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewType

class DishMenuRecyclerViewType(
    //callbacks
) : MenuRecyclerViewType<LayoutDishBinding, Dish> {

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
        //setCallbacks
        return DishViewHolder(LayoutDishBinding.inflate(inflater, parent, false))
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<Dish> = diffCallback

}

class DishViewHolder(
    override val binding: LayoutDishBinding,
) : BaseViewHolder<LayoutDishBinding, Dish>(binding) {

    override fun onBind(item: Dish) {
        with(binding) {
            dishName.text = item.name
            dishConst.text = item.cost
            dishWeight.text = item.weight
        }
    }

}