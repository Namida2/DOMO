package com.example.featureOrder.domain.recyclerView.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.featureOrder.domain.recyclerView.diffCalbacks.MenuAdapterDiffCallback
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem
import com.example.waiterCore.domain.recyclerView.interfaces.BaseViewHolder
import com.example.waiterCore.domain.recyclerView.interfaces.MenuRecyclerViewType

class MenuItemsAdapter(
    private var recyclerViewTypes: List<MenuRecyclerViewType<out ViewBinding, out BaseRecyclerViewItem>>,
) : ListAdapter<BaseRecyclerViewItem, BaseViewHolder<ViewBinding, BaseRecyclerViewItem>>(
    MenuAdapterDiffCallback(recyclerViewTypes)
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ViewBinding, BaseRecyclerViewItem> {
        return recyclerViewTypes.find { it.getLayoutId() == viewType }?.getViewHolder(
            LayoutInflater.from(parent.context),
            parent
        ) as BaseViewHolder<ViewBinding, BaseRecyclerViewItem>
    }


    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, BaseRecyclerViewItem>,
        position: Int,
    ) {
        holder.onBind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return recyclerViewTypes.find { it.isItMe(item) }?.getLayoutId()
            ?: throw IllegalArgumentException("View type not found in recyclerViewItems. Item: $item")
    }
}