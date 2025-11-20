package com.example.myapplication.ui.todo

import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodel.TodoViewModel

class TodoFragment : Fragment() {

    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        adapter = TodoAdapter(
            onToggle = { viewModel.toggle(it) },
            onDelete = { viewModel.delete(it) }
        )

        val rv = view.findViewById<RecyclerView>(R.id.todo_list)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewModel.todos.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.add_todo)
        fab.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val edit = EditText(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle("할 일 추가")
            .setView(edit)
            .setPositiveButton("추가") { _, _ ->
                viewModel.addTodo(edit.text.toString())
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
