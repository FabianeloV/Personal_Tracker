package com.fevr.personaltracker.viewModels

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.fevr.personaltracker.roomResources.Expense
import com.fevr.personaltracker.roomResources.ExpenseDatabase
import kotlinx.coroutines.launch

class MoneyTrackerViewModel(application: Application) : AndroidViewModel(application) {
    private val filaUno = listOf("1", "2", "3")
    private val filaDos = listOf("4", "5", "6")
    private val filaTres = listOf("7", "8", "9")
    private val filaCuatro = listOf(".", "0", "C")

    val numbers = listOf(filaUno, filaDos, filaTres, filaCuatro)

    /*
    private val db =
        Room.databaseBuilder(
            context = getApplication<Application>().applicationContext,
            ExpenseDatabase::class.java,
            "expense_database"
        ).build()

    private val expenseDao = db.expenseDao()

    val expenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    fun insertExpense(expense: Expense) = viewModelScope.launch {
        expenseDao.insertExpense(expense)
    }
    */

    // { scope.launch { DataStore(context).incrementCounter(balanceKey, 4.55f) }

    /*val expenses = mutableListOf(
        ExpensesStructure(type = ExpenseType.Comida, description = "Compras varias", value = 15.40f),
        ExpensesStructure(type = ExpenseType.Gas, description = "Gasolina moto", value = 4.20f),
        ExpensesStructure(type = ExpenseType.Renta, description = "Renta agosto", value = 300.0f),
        ExpensesStructure(type = ExpenseType.Salida, description = "Salida dannita", value = 8.50f),
        ExpensesStructure(type = ExpenseType.Otro, description = "Bolsas de lunita", value = 2.50f),
    )*/
}