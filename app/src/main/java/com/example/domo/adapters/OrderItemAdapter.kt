package com.example.domo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.R
import java.lang.IllegalArgumentException

class OrderItemAdapter(var orderItemsArrayList: List<OrderItem>):
    RecyclerView.Adapter<OrderItemAdapter.ViewHolder>(), View.OnClickListener{

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var container_large: RelativeLayout = itemView.findViewById(R.id.order_item_container_large)
        var container: ConstraintLayout = itemView.findViewById(R.id.order_item_container)
        var categoryName = itemView.findViewById<TextView?>(R.id.line)

        var isReady: ImageView = itemView.findViewById(R.id.is_ready)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =

            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_order_item, parent, false
                )
            ).also {
                it.container.setOnClickListener(this)
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = orderItemsArrayList.size

    override fun onClick(p0: View?) {

    }
}


class OrderItem (
    var name: String? = null,
    var categoryName: String? = null,
    var cost: String? = null,
    var weight: String? = null,
    var description: String? = null,
    var isReady: Boolean = false,
    var commentary: String? = null,
    var count: Int = 0
) {
    override fun hashCode(): Int {
        return (name + commentary).hashCode()
    }
    override fun equals(obj: Any?): Boolean {
        if (obj == null || obj.javaClass != OrderItem::class.java) return false
        val orderItem = obj as OrderItem
        return name + commentary == orderItem.name + orderItem.commentary
    }
}