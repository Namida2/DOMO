package entities.recyclerView.interfaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.domo.adapters.viewHoders.BaseViewHolder

interface MenuRecyclerViewItem<Binding: ViewBinding, Item: BaseRecyclerViewItem> {
    fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean
    fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<Binding, Item>
    fun getLayoutId(): Int
}