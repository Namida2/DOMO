package com.example.featureOrder.domain.recyclerView.viewTypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.domain.menu.CategoriesNameHolder
import com.example.core.presentation.recyclerView.adapters.BaseRecyclerViewAdapter
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.presentation.recyclerView.interfaces.BaseViewHolder
import com.example.featureOrder.R
import com.example.featureOrder.databinding.LayoutCategoriesContainerBinding

//TODO: Add layouts
class CategoriesAdapterDelegate :
    BaseAdapterDelegate<LayoutCategoriesContainerBinding, CategoriesNameHolder> {

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesNameHolder> =
        CategoriesMenuViewHolder(
            LayoutCategoriesContainerBinding.inflate(inflater, parent, false)
        )

    override fun getLayoutId(): Int = R.layout.layout_categories_container
    override fun getDiffCallback(): DiffUtil.ItemCallback<CategoriesNameHolder> = diffCallback

    private val diffCallback = object : DiffUtil.ItemCallback<CategoriesNameHolder>() {
        override fun areItemsTheSame(
            oldItem: CategoriesNameHolder,
            newItem: CategoriesNameHolder,
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: CategoriesNameHolder,
            newItem: CategoriesNameHolder,
        ): Boolean = oldItem == newItem

    }

    override fun isItMe(recyclerViewType: BaseRecyclerViewType): Boolean =
        recyclerViewType is CategoriesNameHolder

}

class CategoriesMenuViewHolder(
    override val binding: LayoutCategoriesContainerBinding,
) : BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesNameHolder>(binding) {

    private var itemsAdapter = BaseRecyclerViewAdapter(
        listOf(
            CategoryRecyclerViewType()
        )
    )

    init {
        with(binding.containerRecyclerView) {
            adapter = itemsAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
            setHasFixedSize(true)
        }
    }

    override fun onBind(item: CategoriesNameHolder) {
        itemsAdapter.submitList(item.categories)
    }
}