package entities.menu

import entities.recyclerView.interfaces.BaseRecyclerViewItem

data class CategoriesHolder(
    var categories: List<CategoryName>
) : BaseRecyclerViewItem