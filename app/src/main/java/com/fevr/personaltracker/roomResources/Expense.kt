package com.fevr.personaltracker.roomResources

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_expenses")
data class Expense(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    var type: String,
    var description: String,
    var value: Float
)
