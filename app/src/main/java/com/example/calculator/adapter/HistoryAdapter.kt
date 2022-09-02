package com.example.calculator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.calculator.databinding.ItemHistoryBinding
import com.example.calculator.model.History


class HistoryAdapter :
    ListAdapter<History, HistoryViewHolder>(HistoryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}