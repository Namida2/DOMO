package com.example.core.domain.recyclerView.interfaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

interface BaseAdapterDelegate<Binding: ViewBinding, Item: BaseRecyclerViewType> {
    fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean
    fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<Binding, Item>
    fun getLayoutId(): Int
    fun getDiffCallback(): DiffUtil.ItemCallback<Item>
}