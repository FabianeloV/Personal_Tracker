package com.fevr.personaltracker.viewModels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("BALANCE")
class DataStore(private val context: Context) {

    // To make sure there is only one instance
    companion object {
        val BALANCE_USER_KEY = floatPreferencesKey("balance_counter")
    }

    // To get the value of the balance

    val getValue: Flow<Float> = context.dataStore.data.mapNotNull { it[BALANCE_USER_KEY] }

    suspend fun incrementCounter(value: Float) {

        context.dataStore.edit {
            if (it[BALANCE_USER_KEY] == null){
                it[BALANCE_USER_KEY] = 0f
            }
            it[BALANCE_USER_KEY] = it[BALANCE_USER_KEY]!!.plus(value)
        }
    }

    suspend fun decrementCounter(value: Float) {
        context.dataStore.edit {
            if (it[BALANCE_USER_KEY] == null){
                it[BALANCE_USER_KEY] = 0f
            }
            it[BALANCE_USER_KEY] = it[BALANCE_USER_KEY]!!.minus(value)
        }
    }
}

class WorkDatastore(private val context: Context){
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("WorkState")
    }

    fun getBalance(balance: Preferences.Key<Boolean>): Flow<Boolean> {
        val getValue: Flow<Boolean> = context.dataStore.data.mapNotNull { preferences ->
            preferences[balance]
        }
        return getValue
    }

    suspend fun setFalse(balance: Preferences.Key<Boolean>) {
        context.dataStore.edit { preference ->
            preference[balance] = false
        }
    }

    suspend fun setTrue(balance: Preferences.Key<Boolean>) {
        context.dataStore.edit { preference ->
            preference[balance] = true
        }
    }
}