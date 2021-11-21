package com.example.domo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.domo.adapters.viewHoders.BaseViewHolder
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewItem

//TODO: Implements view holders with a common BaseViewHolder
class MenuAdapter(
    private var recyclerViewItems: List<MenuRecyclerViewItem<ViewBinding, BaseRecyclerViewItem>>,
) : ListAdapter<BaseRecyclerViewItem, BaseViewHolder<ViewBinding, BaseRecyclerViewItem>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ViewBinding, BaseRecyclerViewItem> {
        val viewHolder = recyclerViewItems.find { it.getLayoutId() == viewType }?.getViewHolder(
            LayoutInflater.from(parent.context),
            parent
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, BaseRecyclerViewItem>,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return recyclerViewItems.find { it.isItMe(item) }?.getLayoutId()
            ?: throw IllegalArgumentException("View type not found in recyclerViewItems. Item: $item")
    }
}