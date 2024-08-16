package com.fevr.personaltracker.viewModels

import androidx.lifecycle.ViewModel

class MoneyTrackerViewModel : ViewModel() {
    private val filaUno = listOf("1", "2", "3")
    private val filaDos = listOf("4", "5", "6")
    private val filaTres = listOf("7", "8", "9")
    private val filaCuatro = listOf(".", "0", "C")

    val numbers = listOf(filaUno, filaDos, filaTres, filaCuatro)

    // { scope.launch { DataStore(context).incrementCounter(balanceKey, 4.55f) }
    val expenses = mutableListOf(
        Expenses(type = ExpenseType.Comida, description = "Compras varias", value = 15.40f),
        Expenses(type = ExpenseType.Gas, description = "Gasolina moto", value = 4.20f),
        Expenses(type = ExpenseType.Renta, description = "Renta agosto", value = 300.0f),
        Expenses(type = ExpenseType.Salida, description = "Salida dannita", value = 8.50f),
        Expenses(type = ExpenseType.Otro, description = "Bolsas de lunita", value = 2.50f),
    )
}