package com.fevr.personaltracker.roomResources

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(vararg expense: Expense)

    @Query("SELECT * FROM user_expenses")
    fun getAllExpenses():LiveData<List<Expense>>

    @Query("DELETE FROM user_expenses")
    suspend fun deleteAll()
}