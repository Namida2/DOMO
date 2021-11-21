package com.example.domo.adapters.diffCallbacks

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewItem

class MenuAdapterDiffCallback: DiffUtil.ItemCallback<MenuRecyclerViewItem<ViewBinding, BaseRecyclerViewItem>>() {
    override fun areItemsTheSame(
        oldItem: MenuRecyclerViewItem<ViewBinding, BaseRecyclerViewItem>,
        newItem: MenuRecyclerViewItem<ViewBinding, BaseRecyclerViewItem>,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: MenuRecyclerViewItem<ViewBinding, BaseRecyclerViewItem>,
        newItem: MenuRecyclerViewItem<ViewBinding, BaseRecyclerViewItem>,
    ): Boolean {
        TODO("Not yet implemented")
    }
}