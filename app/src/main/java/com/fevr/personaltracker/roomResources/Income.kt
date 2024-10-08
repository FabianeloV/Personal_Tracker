package com.fevr.personaltracker.roomResources

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "user_incomes")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var description: String,
    var value: Float
)
