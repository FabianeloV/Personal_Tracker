package com.fevr.personaltracker.roomResources

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkHoursDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkSplit(workHours: WorkHours)

    @Query("SELECT * FROM user_work_hours")
    fun getAllWorkHours(): LiveData<List<WorkHours>>

    @Query("DELETE FROM user_work_hours")
    suspend fun deleteAll()
}