package com.fevr.personaltracker.screens


import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info500
import com.fevr.personaltracker.ui.theme.Info600
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Primary600
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.roomResources.WorkHours
import com.fevr.personaltracker.viewModels.WorkDatastore
import com.fevr.personaltracker.viewModels.WorkTrackerViewmodel
import com.fevr.personaltracker.workManager.scheduleClearWorks
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun WorkTrackerScreen(viewmodel: WorkTrackerViewmodel = WorkTrackerViewmodel()) {
    val context = LocalContext.current

    //Room database
    val db = viewmodel.getDatabase(context)
    val workDays by db.workDao().getAllWorkHours().observeAsState(initial = emptyList())

    //Tiempo transcurrido
    val timeElapsed = remember { mutableIntStateOf(0) }  // Time elapsed in seconds
    var isRunning by remember { mutableStateOf(false) } // To track if the stopwatch is running
    val handler = remember { Handler(Looper.getMainLooper()) } // Remember handler to avoid recreating it
    
    //Datastore de si se han creado splits de trabajos
    val workStateKey = booleanPreferencesKey("state")
    val workState = WorkDatastore(context).getBalance(workStateKey).collectAsState(initial = false)

    //Horas del dia de hoy
    val todayHours = remember { mutableIntStateOf(0) }

    todayHours.intValue = workDays.sumOf { it.value }

    // Create the Runnable that will increment the time
    val runnable = remember {
        object : Runnable {
            override fun run() {
                if (isRunning) {
                    timeElapsed.intValue++
                    handler.postDelayed(this, 1000L)
                }
            }
        }
    }

    // Start the stopwatch
    fun startStopwatch() {
        timeElapsed.intValue = 0
        isRunning = true
        handler.post(runnable)
    }

    // Stop the stopwatch
    fun stopStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)

        if (!workState.value){
            viewmodel.setStateTrue(context)
            scheduleClearWorks(context)
        }
        viewmodel.insertWorkSplit(db, WorkHours(value = timeElapsed.intValue))
    }

    Surface(
        color = Primary400,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 139.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 45.dp, start = 15.dp, end = 15.dp)
                .fillMaxWidth()
        ) {
            TodayWorkHours(todayHours)
        }

        Surface(
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            shadowElevation = 20.dp,
            modifier = Modifier.padding(top = 150.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(top = 5.dp)) {
                    WorkCounterCard(timeElapsed, isRunning) {
                        if (isRunning) {
                            stopStopwatch()
                        } else {
                            startStopwatch()
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    WorkDataCard(todayHours)
                }
                WorkTitleCard()
                WorkGraphs(workDays)
            }
        }
    }
}

@Composable
fun TodayWorkHours(counter: MutableIntState) {

    val hours = counter.intValue / 3600
    val minutes = (counter.intValue % 3600) / 60
    val seconds = counter.intValue % 60

    val formattedTime = String.format("%02dh %02dm %02ds", hours, minutes, seconds)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Trabajo de hoy",
                    color = Primary400,
                    fontSize = 20.sp
                )

                Text(
                    text = formattedTime,
                    color = Primary500,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun WorkGraphs(workDays: List<WorkHours>) {

    val barras = ArrayList<BarChartData.Bar>()

    workDays.forEach { bar ->
        barras.add(
            BarChartData.Bar(
                label = (workDays.indexOf(bar)+1).toString(),
                value = (bar.value % 3600)/60.toFloat(),
                color = Info400
            )
        )
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        ),
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 10.dp, end = 10.dp)
        ) {
            BarChart(
                barChartData = BarChartData(barras),
                modifier = Modifier
                    .padding(10.dp)
                    .height(200.dp),
                animation = simpleChartAnimation(),
                labelDrawer = SimpleValueDrawer(drawLocation = SimpleValueDrawer.DrawLocation.XAxis)
            )
        }
    }
}

@Composable
fun WorkTitleCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        )
    ) {
        Text(
            text = "Patrón de trabajo diario",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = Info500,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun WorkDataCard(counter: MutableIntState) {
    val value = counter.intValue * 0.25

    val hours = value / 3600
    val minutes = (value % 3600) / 60

    val formattedTime = String.format("%02dh %02dm", hours.toInt(), minutes.toInt())

    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(text = "Trabajo enfocado", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Info400)

            Column(modifier = Modifier.padding(top = 5.dp)) {
                Text(text = formattedTime, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Info500)
                Text(text = "25% total", fontSize = 16.sp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun WorkCounterCard(counter: MutableIntState, state:Boolean, click:()->Unit) {
    val hours = counter.intValue / 3600
    val minutes = (counter.intValue % 3600) / 60
    val seconds = counter.intValue % 60

    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Counter",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = Info400
                )
                Text(
                    text = formattedTime,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Info500,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Text(text = "Esta sesión", fontSize = 16.sp, color = Color.LightGray)
            }

            OutlinedIconButton(
                onClick = { click() },
                border = IconButtonDefaults.outlinedIconButtonBorder(enabled = true),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(62.dp)
            ) {
                Icon(
                    imageVector = if (state) Icons.Rounded.Refresh else Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = if (state) Primary600 else Info600
                )
            }
        }
    }
}