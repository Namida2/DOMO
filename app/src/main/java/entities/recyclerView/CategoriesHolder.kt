package entities.recyclerView

import entities.recyclerView.interfaces.BaseRecyclerViewItem

data class CategoriesHolder(
    var categories: List<String>,
) : BaseRecyclerViewItem