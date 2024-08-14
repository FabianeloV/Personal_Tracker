package com.fevr.personaltracker.viewModels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ExpenseType(
    var icon: ImageVector,
    var type: String,
    var color: Color
)
