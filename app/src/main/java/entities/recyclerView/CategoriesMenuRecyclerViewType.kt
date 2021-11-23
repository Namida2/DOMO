package entities.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.domo.R
import entities.recyclerView.interfaces.BaseViewHolder
import com.example.domo.databinding.LayoutCategoryBinding
import entities.CategoriesHolder
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.recyclerView.interfaces.MenuRecyclerViewType

class CategoriesMenuRecyclerViewType :
    MenuRecyclerViewType<LayoutCategoryBinding, CategoriesHolder> {


    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<LayoutCategoryBinding, CategoriesHolder> {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.layout_category
    override fun getDiffCallback(): DiffUtil.ItemCallback<CategoriesHolder> {
        TODO("Not yet implemented")
    }

    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean {
        TODO("Not yet implemented")
    }

}

class CategoriesMenuViewHolder(
    override val binding: LayoutCategoryBinding
): BaseViewHolder<LayoutCategoryBinding, CategoriesHolder>(binding){

    override fun onBind(item: CategoriesHolder) {

    }


}