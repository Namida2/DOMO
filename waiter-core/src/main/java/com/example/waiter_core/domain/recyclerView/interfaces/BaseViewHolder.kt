package com.example.waiter_core.domain.recyclerView.interfaces

import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder<ViewBinding : androidx.viewbinding.ViewBinding, Item : BaseRecyclerViewItem>(
    open val binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(item: Item)
}