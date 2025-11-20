package com.example.myapplication.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Todo

class TodoAdapter(
    private val onToggle: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback()) {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.todo_checkbox)
        val title: TextView = itemView.findViewById(R.id.todo_title)
        val deleteButton: ImageButton = itemView.findViewById(R.id.todo_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        holder.title.text = todo.title
        holder.checkBox.isChecked = todo.isDone

        holder.checkBox.setOnClickListener {
            onToggle(todo)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(todo)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}
