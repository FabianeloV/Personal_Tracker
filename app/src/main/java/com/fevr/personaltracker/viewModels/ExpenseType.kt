package com.fevr.personaltracker.viewModels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info500
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Success400
import com.fevr.personaltracker.ui.theme.Success500
import com.fevr.personaltracker.ui.theme.Warning400

sealed class ExpenseType(
    var icon: ImageVector,
    var type: String,
    var color: Color
) {
    data object Ropa:ExpenseType(
        icon = Icons.Outlined.Face, type = "Ropa", color = Primary400
    )

    data object Comida:ExpenseType(
        icon = Icons.Outlined.ShoppingCart, type = "Comida", color = Info400
    )

    data object Renta:ExpenseType(
        icon = Icons.Outlined.Home, type = "Renta", color = Success400
    )

    data object Salida:ExpenseType(
        icon = Icons.Outlined.Star, type = "Salida", color = Warning400
    )

    data object Servicios:ExpenseType(
        icon = Icons.Outlined.Info, type = "Servicios", color = Primary500
    )

    data object Gas:ExpenseType(
        icon = Icons.Outlined.Place, type = "Gasolina", color = Info500
    )

    data object Otro:ExpenseType(
        icon = Icons.Outlined.Menu, type = "Otro", color = Success500
    )
}
