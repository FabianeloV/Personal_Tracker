package com.fevr.personaltracker.roomResources

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class], version = 2)
abstract class ExpenseDatabase:RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
}