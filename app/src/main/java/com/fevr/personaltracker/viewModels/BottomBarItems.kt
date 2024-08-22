package com.fevr.personaltracker.viewModels

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info800


@Immutable
data class BottomBarItems(
    var selectedIcon: Int,
    var route: String,
    var selectedColor: Color = Info400,
    var unselectedColor: Color = Info800
)