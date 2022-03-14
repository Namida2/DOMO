package com.example.core.domain.menu

import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

data class CategoriesNameHolder(
    var categories: List<CategoryName>
) : BaseRecyclerViewType