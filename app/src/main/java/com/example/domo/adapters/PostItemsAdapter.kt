package com.example.domo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.adapters.diffCallbacks.PostAdapterDiffCallback
import com.example.domo.databinding.LayoutPostItemBinding
import com.example.waiterCore.domain.tools.constants.EmployeePosts
import entities.PostItem

class PostItemsAdapter(
    var postItemsList: MutableList<PostItem>,
    var setSelectedPost: (post: String) -> Unit
) : RecyclerView.Adapter<PostItemsAdapter.ViewHolder>(), View.OnClickListener {

    private var lastSelectedPost: String = EmployeePosts.COOK

    class ViewHolder(
        val binding: LayoutPostItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            LayoutPostItemBinding.inflate(inflater, parent, false)
        ).also {
            it.binding.postContainer.setOnClickListener(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.selectedView.visibility = postItemsList[position].visibility
        holder.binding.postNameTextView.text = postItemsList[position].postName
        holder.binding.root.tag = postItemsList[position].postName
    }

    override fun getItemCount(): Int = postItemsList.size

    override fun onClick(view: View?) {
        val newList = ArrayList<PostItem>(postItemsList)
        newList.find {
            it.postName == view?.tag
        }?.let { newSelectedPostItem ->
            if (newSelectedPostItem.visibility == View.VISIBLE) return
            val oldSelectedPostItem = newList.find { it.postName == lastSelectedPost }!!
            val indexOfNewVisibility = newList.indexOf(newSelectedPostItem)
            val indexOfOldVisibility = newList.indexOf(oldSelectedPostItem)
            newList[indexOfNewVisibility] = newSelectedPostItem.copy(visibility = View.VISIBLE)
            newList[indexOfOldVisibility] = oldSelectedPostItem.copy(visibility = View.INVISIBLE)
            lastSelectedPost = view?.tag as String
            setSelectedPost(view.tag as String)
        }
        val diffCallback = PostAdapterDiffCallback(postItemsList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        postItemsList = newList
        diffResult.dispatchUpdatesTo(this)

    }
}





