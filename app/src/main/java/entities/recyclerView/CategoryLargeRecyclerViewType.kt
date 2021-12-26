package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.domo.R
import com.example.domo.databinding.LayoutCatehoryLargeBinding
import entities.menu.CategoryName
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.BaseViewHolder
import entities.recyclerView.interfaces.MenuRecyclerViewType

class CategoryLargeRecyclerViewType : MenuRecyclerViewType<LayoutCatehoryLargeBinding, CategoryName> {

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean =
        recyclerViewItem is CategoryName

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCatehoryLargeBinding, CategoryName> {
        return CategoryLargeViewHolder(
            LayoutCatehoryLargeBinding.inflate(inflater, parent, false)
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
    override val binding: LayoutCatehoryLargeBinding,
) : BaseViewHolder<LayoutCatehoryLargeBinding, CategoryName>(binding) {

    override fun onBind(item: CategoryName) {
        binding.categoryName.text = item.name
    }
}