package com.example.domo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.domo.adapters.diffCallbacks.MenuAdapterDiffCallback
import entities.recyclerView.interfaces.BaseViewHolder
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewType

class MenuAdapter(
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