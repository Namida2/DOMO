package com.example.domo.adapters.diffCallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.domo.views.PostItem

class PostAdapterDiffCallback(
    private val oldList: List<PostItem>,
    private val newList: List<PostItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPostItem = oldList[oldItemPosition]
        val newPostItem = newList[newItemPosition]
        return oldPostItem.postName == newPostItem.postName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPostItem = oldList[oldItemPosition]
        val newPostItem = newList[newItemPosition]
        return oldPostItem.visibility == newPostItem.visibility
    }

}