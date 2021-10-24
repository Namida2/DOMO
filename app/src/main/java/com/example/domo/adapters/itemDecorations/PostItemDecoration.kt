package com.example.domo.adapters.itemDecorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.adapters.PostItemsAdapter

class PostItemDecoration(
    private val smallMargin: Int,
    private val bigMargin: Int
        ) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as PostItemsAdapter
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return
        when (position) {
            0 -> {
                outRect.left = bigMargin
                outRect.right = smallMargin
            }
            adapter.postItemsList.size - 1 -> {
                outRect.left = smallMargin
                outRect.right = bigMargin
            }
            else -> {
                outRect.left = smallMargin
                outRect.right = smallMargin
            }
        }

    }
}
