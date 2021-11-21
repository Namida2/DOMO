package com.example.domo.adapters.viewHoders

import androidx.recyclerview.widget.RecyclerView
import entities.recyclerView.interfaces.BaseRecyclerViewItem


abstract class BaseViewHolder<ViewBinding : androidx.viewbinding.ViewBinding, Item : BaseRecyclerViewItem>(
    open val binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(item: Item)
}