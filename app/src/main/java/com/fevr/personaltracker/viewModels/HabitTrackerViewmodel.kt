package com.fevr.personaltracker.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.fevr.personaltracker.roomResources.ActivitiesToDo
import com.fevr.personaltracker.roomResources.MoneyTrackerDatabase
import kotlinx.coroutines.launch

class HabitTrackerViewModel:ViewModel() {

    fun getDatabase(context: Context): MoneyTrackerDatabase {
        return Room.databaseBuilder(
            context,
            MoneyTrackerDatabase::class.java, "app-database"
        ).fallbackToDestructiveMigration().build()
    }

    fun insertActivity(db:MoneyTrackerDatabase, activity:ActivitiesToDo)=viewModelScope.launch {
        db.activitiesDao().insertActivity(activity)
    }

    fun deleteActivity(db: MoneyTrackerDatabase, activity: ActivitiesToDo)=viewModelScope.launch {
        db.activitiesDao().deleteActivity(activity)
    }

    fun updateActivityState(db: MoneyTrackerDatabase, activity: ActivitiesToDo)=viewModelScope.launch {
        db.activitiesDao().update(ActivitiesToDo(id = activity.id, description = activity.description, state = true))
    }

    fun updateActivityStateNight(db: MoneyTrackerDatabase, activity: ActivitiesToDo)=viewModelScope.launch {
        db.activitiesDao().update(ActivitiesToDo(id = activity.id, description = activity.description, state = false))
    }
}