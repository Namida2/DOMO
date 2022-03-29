package com.example.core.presentation.recyclerView.adapterDelegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.R
import com.example.core.databinding.LayoutDishBinding
import com.example.core.domain.entities.menu.Dish
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder

class DishesAdapterDelegate(
    private val onDishSelected: (dish: Dish) -> Unit,
) : BaseAdapterDelegate<LayoutDishBinding, Dish>, View.OnClickListener {

    private val diffCallback = object : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean = oldItem == newItem
    }

    override fun getLayoutId(): Int = R.layout.layout_dish

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean = recyclerViewType is Dish

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutDishBinding, Dish> {
        return DishViewHolder(
            LayoutDishBinding.inflate(inflater, parent, false).also {
                it.root.setOnClickListener(this)
            }
        )
    }

    override fun getDiffCallback(): DiffUtil.ItemCallback<Dish> = diffCallback

    override fun onClick(view: View?) {
        onDishSelected.invoke(view?.tag as Dish)
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
            root.tag = item
        }
    }
}