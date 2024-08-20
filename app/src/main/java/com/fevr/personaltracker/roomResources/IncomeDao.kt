package com.fevr.personaltracker.roomResources

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIncome(income: Income)

    @Delete
    suspend fun deleteIncome(vararg income: Income)

    @Query("SELECT * FROM user_incomes")
    fun getAllExpenses(): LiveData<List<Income>>

    @Query("DELETE FROM user_incomes")
    suspend fun deleteAll()
}