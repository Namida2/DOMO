package com.example.domo.adapters.viewHoders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseViewHolder<ViewBinding: androidx.viewbinding.ViewBinding, Item>(
    private var binding: ViewBinding
): RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind()

}