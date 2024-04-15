package com.fevr.personaltracker

import androidx.compose.ui.graphics.Color
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary800

data class BottomBarItems(
    var selectedIcon: Int,
    var route: String,
    var selectedColor: Color = Primary400,
    var unselectedColor: Color = Primary800
)