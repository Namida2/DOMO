package com.example.waiterCore.domain.recyclerView.interfaces

import androidx.recyclerview.widget.RecyclerView

//TODO: Add an universal adapter
abstract class BaseViewHolder<ViewBinding : androidx.viewbinding.ViewBinding, Item : BaseRecyclerViewItem>(
    open val binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(item: Item)
}