package com.fevr.personaltracker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItems(
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector,
    var route: String,
    var selectedColor: Color
)