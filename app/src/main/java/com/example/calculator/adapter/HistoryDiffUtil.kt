package com.example.calculator.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.calculator.model.History

class HistoryDiffUtil : DiffUtil.ItemCallback<History>() {
    override fun areItemsTheSame(
        oldItem: History,
        newItem: History
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: History,
        newItem: History
    ): Boolean {
        return oldItem == newItem
    }
}