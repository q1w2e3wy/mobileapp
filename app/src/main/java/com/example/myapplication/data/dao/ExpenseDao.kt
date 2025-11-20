package com.example.myapplication.data.dao

import androidx.lifecycle.LiveData
import com.example.myapplication.data.model.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM Expense ORDER BY date DESC")
    fun getAll(): LiveData<List<Expense>>

    @Insert
    suspend fun insert(item: Expense)

    @Delete
    suspend fun delete(item: Expense)
}
