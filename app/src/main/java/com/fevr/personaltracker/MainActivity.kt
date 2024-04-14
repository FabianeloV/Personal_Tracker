package com.fevr.personaltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScaffold()
        }
    }
}

@Composable
fun MainScaffold() {
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomItems = listOf(
        BottomBarItems(
            selectedIcon = Icons.Rounded.Home,
            unselectedIcon = Icons.Outlined.Home,
            description = "Money tracker"
        ),
        BottomBarItems(
            selectedIcon = Icons.Rounded.Star,
            unselectedIcon = Icons.Outlined.Star,
            description = "Sleep tracker"
        ),
        BottomBarItems(
            selectedIcon = Icons.Rounded.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            description = "Habit tracker"
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                bottomItems.forEachIndexed { index, bottomBarItems ->
                    NavigationBarItem(
                        modifier = Modifier.clip(CircleShape),
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == index) bottomBarItems.selectedIcon else bottomBarItems.unselectedIcon,
                                contentDescription = bottomBarItems.description
                            )
                        }
                    )

                }
            }
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Example of a scaffold with a bottom app bar."
        )
    }
}