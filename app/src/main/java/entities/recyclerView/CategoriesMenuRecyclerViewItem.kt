package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.domo.R
import com.example.domo.adapters.viewHoders.BaseViewHolder
import com.example.domo.databinding.LayoutCategoryBinding
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewItem

class CategoriesMenuRecyclerViewItem :
    MenuRecyclerViewItem<LayoutCategoryBinding, CategoriesHolder> {
    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem) {
        TODO("Not yet implemented")
    }

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoryBinding, CategoriesHolder> {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.layout_category

}

class CategoriesMenuViewHolder(
    private val binding: LayoutCategoryBinding
): BaseViewHolder<LayoutCategoryBinding, CategoriesHolder>(binding){

    override fun onBind(item: CategoriesHolder) {

    }


}