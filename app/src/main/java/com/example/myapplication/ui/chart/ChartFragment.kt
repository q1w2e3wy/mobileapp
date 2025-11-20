package com.example.myapplication.ui.chart

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodel.ExpenseViewModel

class ChartFragment : Fragment() {

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]

        val chart = view.findViewById<BarChart>(R.id.chart)

        viewModel.expenses.observe(viewLifecycleOwner) { list ->
            val entries = list.mapIndexed { index, item ->
                BarEntry(index.toFloat(), item.amount.toFloat())
            }

            val dataSet = BarDataSet(entries, "지출")
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    }
}
