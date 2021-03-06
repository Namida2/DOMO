package com.example.core.presentation.recyclerView.itemDecorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter

class SimpleListItemDecoration(
    private val largeMargin: Int,
    private val smallMargin: Int,
    private val bottomMargin: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as BaseRecyclerViewAdapter
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.top = largeMargin
                outRect.bottom = smallMargin
            }
            adapter.itemCount - 1 -> {
                outRect.top = smallMargin
                outRect.left = smallMargin
                outRect.right = smallMargin
                outRect.bottom = bottomMargin
            }
            else -> {
                outRect.top = smallMargin
                outRect.bottom = smallMargin
            }
        }
        outRect.left = smallMargin
        outRect.right = smallMargin
    }
}