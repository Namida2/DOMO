package com.example.featureRegistration.domain.recyclerView

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.entities.tools.constants.EmployeePosts
import com.example.featureRegistration.databinding.LayoutPostItemBinding
import com.example.featureRegistration.domain.PostItem

class PostItemsAdapter(
    private val images: List<BitmapDrawable>,
    var postItemsList: MutableList<PostItem>,
    private var setSelectedPost: (post: String) -> Unit
) : RecyclerView.Adapter<PostItemsAdapter.ViewHolder>(), View.OnClickListener {

    private val cookImagePosition = 0
    private val waiterImagePosition = 1
    private val administratorImagePosition = 2
    private var lastSelectedPost: String = EmployeePosts.COOK.value

    class ViewHolder(
        val binding: LayoutPostItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            LayoutPostItemBinding.inflate(inflater, parent, false)
        ).also {
            it.binding.root.setOnClickListener(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            selectedView.visibility = postItemsList[position].visibility
            postNameTextView.text = postItemsList[position].postName
            root.tag = postItemsList[position].postName
            when (postItemsList[position].postName) {
                EmployeePosts.COOK.value -> {
                    postContainer.background = images[cookImagePosition]
                }
                EmployeePosts.WAITER.value -> {
                    postContainer.background = images[waiterImagePosition]
                }
                EmployeePosts.ADMINISTRATOR.value -> {
                    postContainer.background = images[administratorImagePosition]
                }
            }
        }
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





