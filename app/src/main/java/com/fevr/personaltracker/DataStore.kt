package com.fevr.personaltracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull


class DataStore(private val context: Context) {

    // To make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Balance")
    }

    // To get the value of the balance
    fun getBalance(balance: Preferences.Key<Float>): Flow<Float> {
        val getValue: Flow<Float> = context.dataStore.data.mapNotNull { preferences ->
            preferences[balance]
        }
        return getValue
    }

    suspend fun incrementCounter(balance: Preferences.Key<Float>, value: Float) {
        context.dataStore.edit { preference ->
            preference[balance] = preference[balance]!!.plus(value)
        }
    }

    suspend fun decrementCounter(balance: Preferences.Key<Float>, value: Float) {
        context.dataStore.edit { preference ->
            preference[balance] = preference[balance]!!.minus(value)
        }
    }
}