package com.example.calculator.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ItemHistoryBinding
import com.example.calculator.model.History

class HistoryViewHolder(private val binding: ItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: History) {
        binding.input.text = item.input
        binding.result.text = item.result
    }
}