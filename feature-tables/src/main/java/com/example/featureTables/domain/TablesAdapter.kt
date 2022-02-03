package com.example.featureTables.domain

import com.example.featureTables.databinding.LayoutTableBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TablesAdapter(
    private val tablesCount: Int,
    val navigateToOrderFragment: (viewOwner: ViewOwner) -> Unit,
) : RecyclerView.Adapter<TablesAdapter.TableViewHolder>(), View.OnClickListener {

    class TableViewHolder(val binding: LayoutTableBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutTableBinding.inflate(layoutInflater, parent, false)
        binding.tableContainer.setOnClickListener(this)
        return TableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.binding.tableNumber.text = position.toString()
        holder.binding.tableContainer.tag = position
        holder.binding.root.transitionName = position.toString()
    }

    override fun getItemCount(): Int = tablesCount

    override fun onClick(view: View) {
        navigateToOrderFragment(ViewOwner(view))
    }

    //TODO: Remove context
    class ViewOwner(private val view: View) {
        fun getView(context: Context): View = view
    }

}