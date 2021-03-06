package com.example.featureOrder.presentation.recyclerView.itemDecorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.featureOrder.presentation.recyclerView.adapters.TablesAdapter

class TablesItemDecorations(
    private val topMargin: Int,
    private val largeMargin: Int,
    private val smallMargin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as TablesAdapter
        val position = parent.getChildAdapterPosition(view)
        when {
            position == 0 -> {
                outRect.top = topMargin
                outRect.left = largeMargin
                outRect.right = smallMargin
            }
            position == 1 -> {
                outRect.top = topMargin
                outRect.left = smallMargin
                outRect.right = largeMargin
            }
            position % 2 == 0 -> {
                outRect.top = largeMargin
                outRect.left = largeMargin
                outRect.right = smallMargin
            }
            position % 2 == 1 -> {
                outRect.top = largeMargin
                outRect.left = smallMargin
                outRect.right = largeMargin
            }
        }
        if (position == adapter.itemCount - 1 || (adapter.itemCount % 2 == 0 && position == adapter.itemCount - 2))
            outRect.bottom = largeMargin
    }
}