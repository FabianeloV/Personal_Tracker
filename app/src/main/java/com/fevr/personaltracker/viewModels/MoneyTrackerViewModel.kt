package com.fevr.personaltracker.viewModels

import android.app.Application
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.fevr.personaltracker.roomResources.Expense
import com.fevr.personaltracker.roomResources.MoneyTrackerDatabase
import com.fevr.personaltracker.roomResources.ExpenseType
import com.fevr.personaltracker.roomResources.Income
import kotlinx.coroutines.launch

class MoneyTrackerViewModel(application: Application) : AndroidViewModel(application) {
    private val filaUno = listOf("1", "2", "3")
    private val filaDos = listOf("4", "5", "6")
    private val filaTres = listOf("7", "8", "9")

    val numbers = listOf(filaUno, filaDos, filaTres)

    //Funcion para recuperar la base de datos de los gastos
    fun getDatabase(context: Context): MoneyTrackerDatabase {
        return Room.databaseBuilder(
            context,
            MoneyTrackerDatabase::class.java, "app-database"
        ).fallbackToDestructiveMigration().build()
    }

    val expensesType = listOf(
        ExpenseType.Ropa,
        ExpenseType.Comida,
        ExpenseType.Renta,
        ExpenseType.Salida,
        ExpenseType.Servicios,
        ExpenseType.Gas,
        ExpenseType.Otro
    )

    //Funcion para insertar un gasto en la base de datos, se le debe pasar un gasto de tipo Expense y la base recuperada db
    fun insertExpense(expense: Expense, db: MoneyTrackerDatabase) = viewModelScope.launch {
       db.expenseDao().insertExpense(expense)
    }

    fun insertIncome(income: Income, db: MoneyTrackerDatabase) = viewModelScope.launch {
        db.incomeDao().insertIncome(income)
    }

    fun decreaseTotal(value:Float, context:Context) = viewModelScope.launch {
        DataStore(context).decrementCounter(value)
    }

    fun increaseTotal(value: Float, context: Context) = viewModelScope.launch {
        DataStore(context).incrementCounter(value)
    }

    fun getIcon(type:String):ImageVector{
        return when (type){
            "Ropa" -> Icons.Outlined.Face
            "Comida" -> Icons.Outlined.ShoppingCart
            "Renta" -> Icons.Outlined.Home
            "Servicios" -> Icons.Outlined.Info
            "Gas" -> Icons.Outlined.Place
            "Salida" -> Icons.Outlined.Favorite
            "Otro" -> Icons.Outlined.Star

            else -> {
                Icons.Outlined.Star
            }
        }
    }
}