package com.fevr.personaltracker.roomResources

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class,Income::class], version = 3)
abstract class MoneyTrackerDatabase:RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun incomeDao(): IncomeDao
}