package com.fevr.personaltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
    val navController = rememberNavController()

    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomItems = listOf(
        BottomBarItems(
            selectedIcon = Icons.Rounded.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "MoneyTracker"
        ),
        BottomBarItems(
            selectedIcon = Icons.Rounded.Star,
            unselectedIcon = Icons.Outlined.Star,
            route = "SleepTracker",
        ),
        BottomBarItems(
            selectedIcon = Icons.Rounded.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            route = "HabitTracker"
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 35.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    bottomItems.forEachIndexed { index, bottomBarItems ->
                        FloatingActionButton(
                            onClick = { selectedItem = index
                                      navController.navigate(bottomBarItems.route)},
                            shape = CircleShape,
                            elevation = if (selectedItem == index) FloatingActionButtonDefaults.elevation(15.dp) else FloatingActionButtonDefaults.elevation(1.dp),
                            containerColor = Color.White,
                            modifier = Modifier
                                .size(64.dp)
                                .absoluteOffset(y = if (selectedItem == index) (-5).dp else 0.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedItem == index) bottomBarItems.selectedIcon else bottomBarItems.unselectedIcon,
                                contentDescription = bottomBarItems.route,
                                tint = if (selectedItem == index) bottomBarItems.selectedColor else bottomBarItems.unselectedColor
                            )
                        }
                    }
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "MoneyTracker",
            modifier = Modifier.padding(paddingValues = paddingValues)) {
            composable("MoneyTracker") {
                MoneyTrackerScreen(
                    navController
                )
            }
            composable("SleepTracker") {
                SleepTrackerScreen(
                    navController
                )
            }
            composable("HabitTracker") {
                HabitTrackerScreen(
                    navController
                )
            }
        }
    }
}