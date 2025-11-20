package com.example.myapplication.data.database

import android.content.Context
import com.example.myapplication.data.dao.ExpenseDao
import com.example.myapplication.data.dao.TodoDao
import com.example.myapplication.data.model.Expense
import com.example.myapplication.data.model.Todo

@Database(entities = [Todo::class, Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDB(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}
