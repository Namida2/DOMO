package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.R
import com.example.domo.adapters.MenuItemsAdapter
import com.example.domo.databinding.LayoutCategoriesContainerBinding
import entities.CategoriesHolder
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
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onBind(item: CategoriesHolder) {
        itemsAdapter.submitList(item.categories)
    }
}