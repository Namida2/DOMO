package com.example.core.domain.order

import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.domain.tools.constants.FirestoreConstants.DOCUMENT_ORDER_ITEM_DELIMITER
import com.google.firebase.firestore.Exclude

data class OrderItem(
    var dishId: Int,
    var count: Int,
    var commentary: String,
    var isReady: Boolean = false,
) : BaseRecyclerViewType {
    constructor() : this(0, 0, "")

    fun getOrderIemId() =
        dishId.toString() + DOCUMENT_ORDER_ITEM_DELIMITER + commentary

    override fun equals(other: Any?): Boolean =
        if (other !is OrderItem) false
        else dishId == other.dishId && commentary == other.commentary && isReady == other.isReady

    override fun hashCode(): Int = "$dishId: $commentary".hashCode()
}


