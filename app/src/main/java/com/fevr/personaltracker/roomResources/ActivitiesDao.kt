package com.fevr.personaltracker.roomResources

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ActivitiesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivity(activity: ActivitiesToDo)

    @Delete
    suspend fun deleteActivity(vararg activity: ActivitiesToDo)

    @Query("SELECT * FROM user_activities")
    fun getAllExpenses(): LiveData<List<ActivitiesToDo>>

    @Query("DELETE FROM user_activities")
    suspend fun deleteAll()

    @Update
    suspend fun update(activity: ActivitiesToDo)
}