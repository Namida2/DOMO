package com.example.featureMenuDialog.presentation.recyclerView

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.featureMenuDialog.R

class ItemTouchCallback(
    private val onItemDelete: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemDelete(viewHolder.absoluteAdapterPosition - 1)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = if (viewHolder.itemViewType == R.layout.layout_dish)
        super.getSwipeDirs(recyclerView, viewHolder)
    else ItemTouchHelper.ACTION_STATE_IDLE

}