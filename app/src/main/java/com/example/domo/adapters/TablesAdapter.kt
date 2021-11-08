package com.example.domo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.R
import com.example.domo.databinding.LayoutTableBinding
import com.example.domo.views.log

class TablesAdapter(private val tablesCount: Int, val navigateToOrderFragment: (view: View?) -> Unit): RecyclerView.Adapter<TablesAdapter.TableViewHolder>(), View.OnClickListener {

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
        if(position == 5) holder.binding.root.transitionName = "start"
    }

    override fun getItemCount(): Int = tablesCount

    override fun onClick(view: View?) {
        navigateToOrderFragment(view)
    }


}