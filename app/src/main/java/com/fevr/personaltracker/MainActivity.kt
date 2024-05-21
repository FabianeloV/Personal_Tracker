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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fevr.personaltracker.screens.HabitTrackerScreen
import com.fevr.personaltracker.screens.MoneyTrackerScreen
import com.fevr.personaltracker.screens.SleepTrackerScreen
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info800
import com.fevr.personaltracker.ui.theme.Success400
import com.fevr.personaltracker.ui.theme.Success800
import com.fevr.personaltracker.ui.theme.Warning400
import com.fevr.personaltracker.ui.theme.Warning800

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
            selectedIcon = R.drawable.baseline_attach_money_24,
            route = "MoneyTracker",
            selectedColor = Success400,
            unselectedColor = Success800
        ),
        BottomBarItems(
            selectedIcon = R.drawable.baseline_nights_stay_24,
            route = "SleepTracker",
            selectedColor = Info400,
            unselectedColor = Info800
        ),
        BottomBarItems(
            selectedIcon = R.drawable.baseline_calendar_today_24,
            route = "HabitTracker",
            selectedColor = Warning400,
            unselectedColor = Warning800
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
                            elevation = if (selectedItem == index) FloatingActionButtonDefaults.elevation(15.dp) else FloatingActionButtonDefaults.elevation(2.dp),
                            containerColor = Color.White,
                            modifier = Modifier
                                .size(64.dp)
                                .absoluteOffset(y = if (selectedItem == index) (-5).dp else 0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = bottomBarItems.selectedIcon),
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
                MoneyTrackerScreen()
            }
            composable("SleepTracker") {
                SleepTrackerScreen()
            }
            composable("HabitTracker") {
                HabitTrackerScreen()
            }
        }
    }
}