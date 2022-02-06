package com.example.featureOrder.domain.recyclerView.viewTypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.featureOrder.R
import com.example.featureOrder.databinding.LayoutCategoryLargeBinding
import com.example.waiterCore.domain.menu.CategoryName
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem
import com.example.waiterCore.domain.recyclerView.interfaces.BaseViewHolder
import com.example.waiterCore.domain.recyclerView.interfaces.MenuRecyclerViewType

class CategoryLargeRecyclerViewType :
    MenuRecyclerViewType<LayoutCategoryLargeBinding, CategoryName> {

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean =
        recyclerViewItem is CategoryName

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoryLargeBinding, CategoryName> {
        return CategoryLargeViewHolder(
            LayoutCategoryLargeBinding.inflate(inflater, parent, false)
        )
    }

    override fun getLayoutId(): Int = R.layout.layout_category

    override fun getDiffCallback(): DiffUtil.ItemCallback<CategoryName> = diffCallBack
    private val diffCallBack = object : DiffUtil.ItemCallback<CategoryName>() {
        override fun areItemsTheSame(oldItem: CategoryName, newItem: CategoryName): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: CategoryName, newItem: CategoryName): Boolean =
            oldItem == newItem
    }
}

class CategoryLargeViewHolder(
    override val binding: LayoutCategoryLargeBinding,
) : BaseViewHolder<LayoutCategoryLargeBinding, CategoryName>(binding) {

    override fun onBind(item: CategoryName) {
        binding.categoryName.text = item.name
    }
}