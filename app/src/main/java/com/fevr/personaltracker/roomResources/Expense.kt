package com.fevr.personaltracker.roomResources

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
@Immutable
@Entity(tableName = "user_expenses")
data class Expense(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    var type: String,
    var description: String,
    var value: Float
)
