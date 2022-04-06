package com.example.core.domain.entities.order

import com.example.core.domain.entities.tools.constants.FirestoreConstants.ORDER_ITEM_ID_DELIMITER
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

data class OrderItem(
    var dishId: Int,
    var count: Int,
    var commentary: String,
    var isReady: Boolean = false,
) : BaseRecyclerViewType {
    constructor() : this(0, 0, "")

    fun getOrderIemId() =
        dishId.toString() + ORDER_ITEM_ID_DELIMITER + commentary

}


