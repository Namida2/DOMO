package com.example.core.presentation.recyclerView.interfaces

import androidx.recyclerview.widget.RecyclerView

//TODO: Add an universal adapter //STOPPED//
abstract class BaseViewHolder<ViewBinding : androidx.viewbinding.ViewBinding, Item : BaseRecyclerViewType>(
    open val binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(item: Item)
}