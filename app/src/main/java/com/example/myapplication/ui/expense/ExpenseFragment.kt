package com.example.myapplication.ui.expense

import android.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodel.ExpenseViewModel

class ExpenseFragment : Fragment() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]

        adapter = ExpenseAdapter { viewModel.delete(it) }

        val rv = view.findViewById<RecyclerView>(R.id.expense_list)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewModel.expenses.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.add_expense)
        fab.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL

        val title = EditText(requireContext())
        title.hint = "지출 항목"

        val amount = EditText(requireContext())
        amount.hint = "가격"
        amount.inputType = InputType.TYPE_CLASS_NUMBER

        layout.addView(title)
        layout.addView(amount)

        AlertDialog.Builder(requireContext())
            .setTitle("지출 추가")
            .setView(layout)
            .setPositiveButton("추가") { _, _ ->
                viewModel.addExpense(title.text.toString(), amount.text.toString().toInt())
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
