package com.fevr.personaltracker.roomResources

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_activities")
data class ActivitiesToDo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var description: String,
    var state:Boolean
)
