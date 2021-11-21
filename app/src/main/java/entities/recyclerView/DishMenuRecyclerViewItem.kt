package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.domo.R
import com.example.domo.adapters.viewHoders.BaseViewHolder
import com.example.domo.databinding.LayoutDishBinding
import entities.Dish
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewItem

class DishMenuRecyclerViewItem(
    //callbacks
) : MenuRecyclerViewItem<LayoutDishBinding, Dish> {

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean = recyclerViewItem is Dish

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutDishBinding, Dish> {
        return DishViewHolder(LayoutDishBinding.inflate(inflater, parent, false))
    }

    override fun getLayoutId(): Int = R.layout.layout_dish

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