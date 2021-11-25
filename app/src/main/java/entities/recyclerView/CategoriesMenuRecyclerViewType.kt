package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.domo.R
import com.example.domo.adapters.MenuItemsAdapter
import com.example.domo.databinding.LayoutCategoriesContainerBinding
import entities.menu.CategoriesHolder
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.BaseViewHolder
import entities.recyclerView.interfaces.MenuRecyclerViewType

class CategoriesMenuRecyclerViewType :
    MenuRecyclerViewType<LayoutCategoriesContainerBinding, CategoriesHolder> {

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesHolder> =
        CategoriesMenuViewHolder(
            LayoutCategoriesContainerBinding.inflate(inflater, parent, false)
        )

    override fun getLayoutId(): Int = R.layout.layout_categories_container
    override fun getDiffCallback(): DiffUtil.ItemCallback<CategoriesHolder> = diffCallback

    private val diffCallback = object : DiffUtil.ItemCallback<CategoriesHolder>() {
        override fun areItemsTheSame(
            oldItem: CategoriesHolder,
            newItem: CategoriesHolder,
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: CategoriesHolder,
            newItem: CategoriesHolder,
        ): Boolean = oldItem == newItem

    }

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean =
        recyclerViewItem is CategoriesHolder

}

class CategoriesMenuViewHolder(
    override val binding: LayoutCategoriesContainerBinding,
) : BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesHolder>(binding) {

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

    override fun onBind(item: CategoriesHolder) {
        itemsAdapter.submitList(item.categories)
    }
}