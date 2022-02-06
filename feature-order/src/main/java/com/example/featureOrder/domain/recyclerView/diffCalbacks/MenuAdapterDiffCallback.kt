package com.example.featureOrder.domain.recyclerView.diffCalbacks

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem
import com.example.waiterCore.domain.recyclerView.interfaces.MenuRecyclerViewType

class MenuAdapterDiffCallback(
    private val recyclerViewTypes: List<MenuRecyclerViewType<out ViewBinding, out BaseRecyclerViewItem>>,
) : DiffUtil.ItemCallback<BaseRecyclerViewItem>() {
    override fun areItemsTheSame(
        oldItem: BaseRecyclerViewItem,
        newItem: BaseRecyclerViewItem,
    ): Boolean =
        if (newItem::class != newItem::class) false
        else getDiffCallback(newItem).areItemsTheSame(oldItem, newItem)


    override fun areContentsTheSame(
        oldItem: BaseRecyclerViewItem,
        newItem: BaseRecyclerViewItem,
    ): Boolean =
        if (newItem::class != newItem::class) false
        else getDiffCallback(newItem).areContentsTheSame(oldItem, newItem)

    private fun getDiffCallback(
        item: BaseRecyclerViewItem,
    ): DiffUtil.ItemCallback<BaseRecyclerViewItem> {
        return recyclerViewTypes.find { it.isItMe(item) }?.getDiffCallback() as? DiffUtil.ItemCallback<BaseRecyclerViewItem>
            ?: throw IllegalArgumentException("DiffCallback not found for this item: $item")
    }

}