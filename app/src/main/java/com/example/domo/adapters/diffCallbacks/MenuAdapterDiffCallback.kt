package com.example.domo.adapters.diffCallbacks

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.waiterCore.domain.recyclerView.interfaces.BaseAdapterDelegate

class MenuAdapterDiffCallback(
    private val recyclerViewTypes: List<BaseAdapterDelegate<out ViewBinding, out BaseRecyclerViewType>>,
) : DiffUtil.ItemCallback<BaseRecyclerViewType>() {
    override fun areItemsTheSame(
        oldType: BaseRecyclerViewType,
        newType: BaseRecyclerViewType,
    ): Boolean =
        if (newType::class != newType::class) false
        else getDiffCallback(newType).areItemsTheSame(oldType, newType)


    override fun areContentsTheSame(
        oldType: BaseRecyclerViewType,
        newType: BaseRecyclerViewType,
    ): Boolean =
        if (newType::class != newType::class) false
        else getDiffCallback(newType).areContentsTheSame(oldType, newType)

    private fun getDiffCallback(
        type: BaseRecyclerViewType,
    ): DiffUtil.ItemCallback<BaseRecyclerViewType> {
        return recyclerViewTypes.find { it.isItMe(type) }?.getDiffCallback() as DiffUtil.ItemCallback<BaseRecyclerViewType>
            ?: throw IllegalArgumentException("DiffCallback not found for this item: $type")
    }

}