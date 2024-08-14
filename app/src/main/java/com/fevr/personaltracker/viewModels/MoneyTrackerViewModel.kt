package com.fevr.personaltracker.viewModels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.lifecycle.ViewModel
import com.fevr.personaltracker.ui.theme.Primary400

class MoneyTrackerViewModel : ViewModel() {
    private val filaUno = listOf("1", "2", "3")
    private val filaDos = listOf("4", "5", "6")
    private val filaTres = listOf("7", "8", "9")
    private val filaCuatro = listOf(".", "0", "C")

    val numbers = listOf(filaUno, filaDos, filaTres, filaCuatro)

    val expenseList: List<ExpenseType> = listOf(
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Ropa", color = Primary400),
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Comida", color = Primary400),
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Renta", color = Primary400),
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Salida", color = Primary400),
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Servicios", color = Primary400),
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Gasolina", color = Primary400),
        ExpenseType(icon = Icons.Outlined.AccountBox, type = "Otro", color = Primary400)
    )
}