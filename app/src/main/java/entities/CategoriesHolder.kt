package entities

import entities.recyclerView.interfaces.BaseRecyclerViewItem

data class CategoriesHolder(
    var categories: List<CategoryName>
) : BaseRecyclerViewItem