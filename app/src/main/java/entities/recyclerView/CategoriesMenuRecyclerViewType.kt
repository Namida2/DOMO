package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.domo.R
import com.example.domo.adapters.MenuItemsAdapter
import com.example.domo.databinding.LayoutCategoriesContainerBinding
import entities.menu.CategoriesNameHolder
import com.example.waiter_core.domain.recyclerView.interfaces.BaseRecyclerViewItem
import com.example.waiter_core.domain.recyclerView.interfaces.BaseViewHolder
import com.example.waiter_core.domain.recyclerView.interfaces.MenuRecyclerViewType

class CategoriesMenuRecyclerViewType :
    MenuRecyclerViewType<LayoutCategoriesContainerBinding, CategoriesNameHolder> {

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

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean =
        recyclerViewItem is CategoriesNameHolder

}

class CategoriesMenuViewHolder(
    override val binding: LayoutCategoriesContainerBinding,
) : BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesNameHolder>(binding) {

    private var itemsAdapter = MenuItemsAdapter(
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