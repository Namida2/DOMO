package com.example.domo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.core.domain.recyclerView.interfaces.BaseAdapterDelegate
import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.domain.recyclerView.interfaces.BaseViewHolder
import com.example.core.domain.tools.constants.OtherStringConstants.VIEW_TYPE_NOT_FOUND
import com.example.domo.adapters.diffCallbacks.MenuAdapterDiffCallback

class Adapter(
    private var recyclerViewTypes: List<BaseAdapterDelegate<out ViewBinding, out BaseRecyclerViewType>>,
) : ListAdapter<BaseRecyclerViewType, BaseViewHolder<ViewBinding, BaseRecyclerViewType>>(
    MenuAdapterDiffCallback(recyclerViewTypes)
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ViewBinding, BaseRecyclerViewType> {
        return recyclerViewTypes.find { it.getLayoutId() == viewType }?.getViewHolder(
            LayoutInflater.from(parent.context),
            parent
        ) as BaseViewHolder<ViewBinding, BaseRecyclerViewType>
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, BaseRecyclerViewType>,
        position: Int,
    ) {
        holder.onBind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return recyclerViewTypes.find { it.isItMe(item) }?.getLayoutId()
            ?: throw IllegalArgumentException(VIEW_TYPE_NOT_FOUND + item)
    }
}