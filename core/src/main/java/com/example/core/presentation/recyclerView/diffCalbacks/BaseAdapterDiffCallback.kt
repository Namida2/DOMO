package com.example.core.presentation.recyclerView.diffCalbacks

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.core.presentation.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

//TODO: Replace old adapters with BaseRecyclerViewAdapter
class BaseAdapterDiffCallback(
    private val recyclerViewTypes: List<BaseAdapterDelegate<out ViewBinding, out BaseRecyclerViewType>>,
) : DiffUtil.ItemCallback<BaseRecyclerViewType>() {
    override fun areItemsTheSame(
        oldType: BaseRecyclerViewType,
        newType: BaseRecyclerViewType,
    ): Boolean =
        if (newType::class != oldType::class) false
        else getDiffCallback(newType).areItemsTheSame(oldType, newType)

    override fun areContentsTheSame(
        oldType: BaseRecyclerViewType,
        newType: BaseRecyclerViewType,
    ): Boolean =
        if (newType::class != oldType::class) false
        else getDiffCallback(newType).areContentsTheSame(oldType, newType)

    private fun getDiffCallback(
        type: BaseRecyclerViewType,
    ): DiffUtil.ItemCallback<BaseRecyclerViewType> {
        return recyclerViewTypes.find {
            it.isItMe(type)
        }?.getDiffCallback() as DiffUtil.ItemCallback<BaseRecyclerViewType>
    }
}