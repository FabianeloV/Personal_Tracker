package com.fevr.personaltracker.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fevr.personaltracker.roomResources.ActivitiesToDo
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info500
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary600
import com.fevr.personaltracker.ui.theme.Primary700
import com.fevr.personaltracker.ui.theme.Primary800
import com.fevr.personaltracker.viewModels.HabitTrackerViewModel
import androidx.compose.ui.unit.sp
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.workManager.scheduleDayChangeWorker
import java.time.LocalDate
import java.util.Locale

@Composable
fun HabitTrackerScreen(viewModel: HabitTrackerViewModel = HabitTrackerViewModel()) {
    val context = LocalContext.current

    val activityText = remember { mutableStateOf("") }

    val db = viewModel.getDatabase(context)
    val activities by db.activitiesDao().getAllExpenses().observeAsState(initial = emptyList())


    Surface(
        color = Primary400,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 139.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 55.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActivityTextField(text = activityText)

            AddActivityButton {
                if (activityText.value.isNotEmpty()) {
                    viewModel.insertActivity(
                        db,
                        ActivitiesToDo(description = activityText.value, state = false)
                    )
                    activityText.value = ""
                } else {
                    Toast.makeText(context, "Campo de actividad vacío", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            shadowElevation = 20.dp,
            modifier = Modifier.padding(top = 150.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                DayCard()

                LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
                    activities.forEach { activity ->
                        item {
                            ActivityCard(activity = activity,
                                {
                                    viewModel.updateActivityState(db, activity)

                                    scheduleDayChangeWorker(context, activity)
                                },
                                {
                                    viewModel.deleteActivity(db, activity)
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ActivityTextField(text: MutableState<String>) {
    OutlinedTextField(
        value = text.value, onValueChange = { text.value = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = null,
                tint = Primary400
            )
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Black,
            color = Primary600,
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Primary800,
            unfocusedIndicatorColor = Primary600,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        placeholder = { Text(text = "Añadir actividad", color = Primary400) },
        shape = CircleShape,
        singleLine = true,
        modifier = Modifier.width(280.dp)
    )
}

@Composable
fun AddActivityButton(click: () -> Unit) {
    FloatingActionButton(
        onClick = { click() },
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(5.dp),
        containerColor = Primary700,
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
    }
}

@Composable
fun ActivityCard(activity: ActivitiesToDo, check: () -> Unit, delete: () -> Unit) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardColors(
            containerColor = Color.White,
            disabledContainerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                onClick = { delete() }, border = IconButtonDefaults.outlinedIconButtonBorder(
                    enabled = false
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete activity",
                    tint = Primary600
                )
            }

            Text(
                text = activity.description, fontWeight = FontWeight.Black, color = Info500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(260.dp),
                textAlign = TextAlign.Center
            )

            Checkbox(
                checked = activity.state,
                onCheckedChange = { check() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Info500,
                    uncheckedColor = Info400,
                    checkmarkColor = Color.White
                )
            )
        }
    }
}

@Composable
fun DayCard() {
    val currentDay by remember { mutableStateOf(LocalDate.now()) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        ),
        modifier = Modifier.padding(top = 7.dp)
    ) {
        Text(
            text = currentDay.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.FULL,
                Locale("es", "ES")
            ).replaceFirstChar { it.uppercase() },
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = Primary500,
            modifier = Modifier.padding(10.dp)
        )
    }
}