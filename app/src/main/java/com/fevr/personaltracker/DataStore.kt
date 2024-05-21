package com.fevr.personaltracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStore(private val context: Context) {

    // To make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Balance")
    }

    // To get the value of the balance
    fun getBalance(balance: Preferences.Key<Int>): Flow<Int?> {
        val getValue: Flow<Int?> = context.dataStore.data.map { preferences ->
            preferences[balance] ?: 0
        }
        return getValue
    }

    suspend fun incrementCounter(balance: Preferences.Key<Int>) {
        context.dataStore.edit { Balance ->
            Balance[balance] = Balance[balance]!! + 1
        }
    }

    suspend fun decrementCounter(balance: Preferences.Key<Int>) {
        context.dataStore.edit { Balance ->
            Balance[balance] = Balance[balance]!! - 1
        }
    }
}