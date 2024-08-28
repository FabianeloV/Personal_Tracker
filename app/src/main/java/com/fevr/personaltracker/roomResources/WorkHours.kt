package com.fevr.personaltracker.roomResources

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "user_work_hours")
data class WorkHours(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val value:Int
)