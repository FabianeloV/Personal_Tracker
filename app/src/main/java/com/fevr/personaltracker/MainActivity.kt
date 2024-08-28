package com.fevr.personaltracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fevr.personaltracker.screens.HabitTrackerScreen
import com.fevr.personaltracker.screens.MoneyTrackerScreen
import com.fevr.personaltracker.screens.WorkTrackerScreen
import com.fevr.personaltracker.ui.theme.PersonalTrackerTheme
import com.fevr.personaltracker.viewModels.BottomBarItems

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PersonalTrackerTheme {
                MainScaffold()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomItems = listOf(
        BottomBarItems(
            selectedIcon = R.drawable.baseline_attach_money_24,
            route = "MoneyTracker"
        ),
        BottomBarItems(
            selectedIcon = R.drawable.baseline_work_24,
            route = "SleepTracker"
        ),
        BottomBarItems(
            selectedIcon = R.drawable.baseline_calendar_today_24,
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
                            onClick = {
                                selectedItem.intValue = index
                                navController.navigate(bottomBarItems.route)
                            },
                            shape = CircleShape,
                            elevation = if (selectedItem.intValue == index) FloatingActionButtonDefaults.elevation(
                                15.dp
                            ) else FloatingActionButtonDefaults.elevation(2.dp),
                            containerColor = Color.White,
                            modifier = Modifier
                                .size(80.dp)
                                .absoluteOffset(y = if (selectedItem.intValue == index) (-7).dp else 0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = bottomBarItems.selectedIcon),
                                contentDescription = bottomBarItems.route,
                                tint = if (selectedItem.intValue == index) bottomBarItems.selectedColor else bottomBarItems.unselectedColor
                            )
                        }
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "MoneyTracker",
        ) {
            composable("MoneyTracker") {
                MoneyTrackerScreen()
            }
            composable("SleepTracker") {
                WorkTrackerScreen()
            }
            composable("HabitTracker") {
                HabitTrackerScreen()
            }
        }

        InfoCard(value = selectedItem)
    }
}

@Composable
fun InfoCard(value: MutableIntState) {
    val showDialog = remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { showDialog.value = true },
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(5.dp),
            containerColor = Color.White,
            contentColor = Color.LightGray,
            modifier = Modifier.padding(start = 35.dp, bottom = 145.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Show info"
            )
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = { },
            title = {
                Text(
                    text = "Información"
                )
            },
            text = {
                Text(
                    text = when (value.intValue) {
                        0 -> "Presione el switch de la parte superior derecha para alternar entre ingresos y egresos, presione el botón de la parte inferior izquierda para agregar un ingreso o egreso deprendiendo de la pantalla en la que se encuentre. Las listas de cada tipo de transacción se muestra en cada pantalla"
                        1 -> "Presione el botón que se encuentra en la tarjeta de <<Contador>> para iniciar un nuevo split de trabajo. Los splits de trabajo que realice durante el día se muestran en la gráfica siguiente. Los splits de cada día se eliminan cada noche para dar paso a los del día siguiente"
                        2 -> "Escriba en el cuadro de texto que actividad diaria le gustaría realizar constantemente. Puede agregar las tareas que desee. Las tareas que han sido marcadas, serán desmarcadas automáticamente cuando el día cambie para que puedan ser marcadas nuevamente"
                        else -> ""
                    }
                )
            }
        )
    }

}
