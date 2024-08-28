package com.fevr.personaltracker.roomResources

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class, Income::class, ActivitiesToDo::class, WorkHours::class], version = 5)
abstract class MoneyTrackerDatabase:RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    abstract fun incomeDao(): IncomeDao

    abstract fun activitiesDao(): ActivitiesDao

    abstract fun workDao(): WorkHoursDao
}