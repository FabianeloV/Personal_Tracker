package com.fevr.personaltracker.viewModels

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.fevr.personaltracker.roomResources.MoneyTrackerDatabase
import com.fevr.personaltracker.roomResources.WorkHours
import kotlinx.coroutines.launch

class WorkTrackerViewmodel:ViewModel() {
    fun getDatabase(context: Context): MoneyTrackerDatabase {
        return Room.databaseBuilder(
            context,
            MoneyTrackerDatabase::class.java, "app-database"
        ).fallbackToDestructiveMigration().build()
    }

    fun setStateTrue(context: Context)=viewModelScope.launch {
        WorkDatastore(context).setTrue(booleanPreferencesKey("state"))
    }
    fun setStateFalse(context: Context)=viewModelScope.launch {
        WorkDatastore(context).setFalse(booleanPreferencesKey("state"))
    }

    fun insertWorkSplit(db:MoneyTrackerDatabase, work:WorkHours) = viewModelScope.launch {
        db.workDao().insertWorkSplit(work)
    }

    fun clearWorkSplits(db: MoneyTrackerDatabase) = viewModelScope.launch {
        db.workDao().deleteAll()
    }
}