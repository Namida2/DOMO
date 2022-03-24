package com.example.featureMenuDialog.presentation.recyclerView.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.menu.CategoryName
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.LayoutCategoryLargeBinding

class CategoryLargeRecyclerViewType(
    private val isAdmin: Boolean,
    private val onAddDishButtonClick: (categoryName: String) -> Unit
) : BaseAdapterDelegate<LayoutCategoryLargeBinding, CategoryName>, View.OnClickListener{

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is CategoryName

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoryLargeBinding, CategoryName> {
        return CategoryLargeViewHolder(
            LayoutCategoryLargeBinding.inflate(inflater, parent, false).also {
                it.addCategoryFab.setOnClickListener(this)
            }, isAdmin
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

    override fun onClick(v: View?) {
        v?.tag?.let { onAddDishButtonClick(it as String) }
    }
}

class CategoryLargeViewHolder(
    override val binding: LayoutCategoryLargeBinding,
    private val isAdmin: Boolean
) : BaseViewHolder<LayoutCategoryLargeBinding, CategoryName>(binding) {

    override fun onBind(item: CategoryName) {
        binding.addCategoryFab.tag = item.name
        binding.categoryName.text = item.name
        if (isAdmin) binding.addCategoryFab.visibility = View.VISIBLE
        else binding.addCategoryFab.visibility = View.GONE
    }
}