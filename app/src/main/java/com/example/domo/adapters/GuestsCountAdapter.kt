package com.example.domo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domo.databinding.LayoutGuestsCountCardBinding

class GuestsCountAdapter(
    private val maxCount: Int,
    private val onCountSelected: (count: Int) -> Unit,
) : RecyclerView.Adapter<GuestsCountAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(val binding: LayoutGuestsCountCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutGuestsCountCardBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.guestsCount.text = position.toString()
        holder.binding.root.tag = position
    }

    override fun getItemCount(): Int = maxCount

    override fun onClick(v: View?) {
       onCountSelected(v?.tag as Int)
    }

}