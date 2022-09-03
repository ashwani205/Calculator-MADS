package com.example.calculator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculator.adapter.HistoryAdapter
import com.example.calculator.databinding.FragmentHistoryBinding
import com.example.calculator.model.History
import java.util.ArrayList


class HistoryFragment : Fragment() {

    private lateinit var mBinding: FragmentHistoryBinding
    private var history: ArrayList<History> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.getParcelableArrayList<History>("history") != null)
                history = it.getParcelableArrayList("history")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHistoryBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.back.setOnClickListener {
            activity?.onBackPressed()
        }
        (activity as AppCompatActivity).supportActionBar?.hide()
        setHistoryAdapter()
    }

    private fun setHistoryAdapter(){
        mBinding.historyRb.layoutManager = LinearLayoutManager(mBinding.root.context,LinearLayoutManager.VERTICAL,false)
        val adapter = HistoryAdapter()
        mBinding.historyRb.adapter = adapter
        adapter.submitList(history)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}