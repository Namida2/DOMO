package com.example.featureMenuDialog.presentation.recyclerView.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.menu.CategoryName
import com.example.core.domain.entities.tools.extensions.precomputeAndSetText
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder
import com.example.featureMenuDialog.R
import com.example.featureMenuDialog.databinding.LayoutCategoryBinding

class CategoryRecyclerViewType (
    private val onCategoryClick: (position: Int) -> Unit
): BaseAdapterDelegate<LayoutCategoryBinding, CategoryName>, View.OnClickListener {

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is CategoryName

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoryBinding, CategoryName> {
        return CategoryViewHolder(
            LayoutCategoryBinding.inflate(inflater, parent, false).also {
                it.root.setOnClickListener(this)
            }
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
        v?.let {
            onCategoryClick.invoke(it.tag as Int)
        }
    }
}

class CategoryViewHolder(
    override val binding: LayoutCategoryBinding,
) : BaseViewHolder<LayoutCategoryBinding, CategoryName>(binding) {

    override fun onBind(item: CategoryName) {
        binding.root.tag = item.position
        binding.categoryName.precomputeAndSetText(item.name)
    }
}