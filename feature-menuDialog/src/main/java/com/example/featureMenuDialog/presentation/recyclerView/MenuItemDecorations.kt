package com.example.featureMenuDialog.presentation.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter

class MenuItemDecorations(
    private val smallMargin: Int,
    private val largeMargin: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as BaseRecyclerViewAdapter
        val position = parent.getChildAdapterPosition(view)
        when (position) {
            0 -> {
                outRect.top = largeMargin
                outRect.bottom = smallMargin
            }
            adapter.currentList.size - 1 -> {
                outRect.top = smallMargin
                outRect.bottom = largeMargin
            }
            else -> {
                outRect.top = smallMargin
                outRect.bottom = smallMargin
            }
        }
        if (position != 0) {
            outRect.left = largeMargin
            outRect.right = smallMargin
        }
    }
}