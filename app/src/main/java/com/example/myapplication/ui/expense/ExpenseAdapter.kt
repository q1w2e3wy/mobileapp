package com.example.myapplication.ui.expense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val onDelete: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.expense_title)
        val amount: TextView = itemView.findViewById(R.id.expense_amount)
        val date: TextView = itemView.findViewById(R.id.expense_date)
        val deleteButton: ImageButton = itemView.findViewById(R.id.expense_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.title.text = expense.title
        holder.amount.text = "${expense.amount}원"

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        holder.date.text = sdf.format(Date(expense.date))

        holder.deleteButton.setOnClickListener {
            onDelete(expense)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}
