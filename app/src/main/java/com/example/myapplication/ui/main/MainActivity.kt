package com.example.myapplication.ui.main

import android.app.Fragment
import android.os.Bundle
import com.example.myapplication.ui.chart.ChartFragment
import com.example.myapplication.ui.expense.ExpenseFragment
import com.example.myapplication.ui.todo.TodoFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TodoFragment())
            .commit()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_todo -> {
                    replace(TodoFragment())
                    true
                }
                R.id.tab_expense -> {
                    replace(ExpenseFragment())
                    true
                }
                R.id.tab_chart -> {
                    replace(ChartFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replace(f: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, f)
            .commit()
    }
}
