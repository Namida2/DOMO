package com.example.waiter_core.domain.menu

import com.example.waiter_core.domain.recyclerView.interfaces.BaseRecyclerViewItem

data class CategoriesNameHolder(
    var categories: List<CategoryName>
) : BaseRecyclerViewItem