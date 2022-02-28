package com.example.featureOrder.domain.recyclerView.viewTypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.menu.CategoryName
import com.example.core.domain.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.domain.recyclerView.interfaces.BaseViewHolder
import com.example.featureOrder.R
import com.example.featureOrder.databinding.LayoutCategoryBinding

class CategoryRecyclerViewType :
    BaseAdapterDelegate<LayoutCategoryBinding, CategoryName> {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is CategoryName

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoryBinding, CategoryName> {
        return CategoryViewHolder(
            LayoutCategoryBinding.inflate(inflater, parent, false)
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

class CategoryViewHolder(
    override val binding: LayoutCategoryBinding,
) : BaseViewHolder<LayoutCategoryBinding, CategoryName>(binding) {

    override fun onBind(item: CategoryName) {
        binding.categoryName.text = item.name
    }
}