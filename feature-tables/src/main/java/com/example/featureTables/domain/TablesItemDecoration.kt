package com.example.featureTables.domain

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class TablesItemDecorations(
    private val smallMargin: Int,
    private val largeMargin: Int,
    private val topMargin: Int
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
            position % 2 == 0 && position == adapter.itemCount - 2 -> {
                outRect.top = largeMargin
                outRect.left = largeMargin
                outRect.right = smallMargin
                outRect.bottom = largeMargin
            }
            position % 2 == 1 && position == adapter.itemCount - 1 -> {
                outRect.top = largeMargin
                outRect.left = smallMargin
                outRect.right = largeMargin
                outRect.bottom = largeMargin
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
    }
}