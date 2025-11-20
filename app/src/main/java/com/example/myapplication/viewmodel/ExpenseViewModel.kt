package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.Expense
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDB(application).expenseDao()
    val expenses = dao.getAll()

    fun addExpense(title: String, amount: Int) = viewModelScope.launch {
        dao.insert(Expense(title = title, amount = amount, date = System.currentTimeMillis()))
    }

    fun delete(item: Expense) = viewModelScope.launch {
        dao.delete(item)
    }
}
