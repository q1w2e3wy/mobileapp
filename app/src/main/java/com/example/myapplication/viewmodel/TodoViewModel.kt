package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.Todo
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDB(application).todoDao()
    val todos: LiveData<List<Todo>> = dao.getAll()

    fun addTodo(title: String) = viewModelScope.launch {
        dao.insert(Todo(title = title))
    }

    fun toggle(todo: Todo) = viewModelScope.launch {
        dao.update(todo.copy(isDone = !todo.isDone))
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        dao.delete(todo)
    }
}
