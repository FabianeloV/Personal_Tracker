package com.fevr.personaltracker.roomResources

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ExpenseType(
    var icon: ImageVector,
    var type: String,
) {
    data object Ropa : ExpenseType(
        icon = Icons.Outlined.Face, type = "Ropa"
    )

    data object Comida : ExpenseType(
        icon = Icons.Outlined.ShoppingCart, type = "Comida"
    )

    data object Renta : ExpenseType(
        icon = Icons.Outlined.Home, type = "Renta"
    )

    data object Salida : ExpenseType(
        icon = Icons.Outlined.Favorite, type = "Salida"
    )

    data object Servicios : ExpenseType(
        icon = Icons.Outlined.Info, type = "Servicios"
    )

    data object Gas : ExpenseType(
        icon = Icons.Outlined.Place, type = "Gasolina"
    )

    data object Otro : ExpenseType(
        icon = Icons.Outlined.Star, type = "Otro"
    )
}
