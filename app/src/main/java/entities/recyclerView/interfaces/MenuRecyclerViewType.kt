package entities.recyclerView.interfaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.domo.adapters.viewHoders.BaseViewHolder

interface MenuRecyclerViewType<Binding: ViewBinding, Item: BaseRecyclerViewItem> {
    fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean
    fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<Binding, Item>
    fun getLayoutId(): Int
    fun getDiffCallback(): DiffUtil.ItemCallback<Item>
}