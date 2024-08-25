package com.fevr.personaltracker.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info500
import com.fevr.personaltracker.ui.theme.Info700
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.ui.theme.Success600
import com.fevr.personaltracker.viewModels.SleepHours
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import java.time.LocalDate
import java.util.Locale

@Composable
fun SleepTrackerScreen() {
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
            LastNightHours()
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
                SleepTitleCard()
                SleepGraphs()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SleepDataCard(text1 = "Sueño profundo", text2 = "1h 45m", porcentaje = 25)
                    SleepDataCard(text1 = "Sueño REM", text2 = "2h 10m", porcentaje = 30)
                }

                Box(modifier = Modifier.padding(top = 10.dp)) {
                    SleepDebtCard()
                }
            }
        }
    }
}

@Composable
fun LastNightHours() {
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
                    text = "Sueño de anoche",
                    color = Primary400,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "7h 23m",
                    color = Primary500,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Calidad de sueño",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )

                Text(
                    text = "Buena",
                    color = Success600,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun SleepGraphs() {
    val currentDay by remember {
        mutableStateOf(
            LocalDate.now().dayOfWeek.getDisplayName(
                java.time.format.TextStyle.FULL,
                Locale("es", "ES")
            ).replaceFirstChar { it.uppercase() }
        )
    }

    val testDays = listOf(
        SleepHours('L', 7f),
        SleepHours('M', 6f),
        SleepHours('M', 7f),
        SleepHours('J', 8f),
        SleepHours('V', 5f),
        SleepHours('S', 5f),
        SleepHours('D', 6f)
    )

    val barras = ArrayList<BarChartData.Bar>()

    testDays.forEach { bar ->
        barras.add(
            BarChartData.Bar(
                label = bar.dayOfWeek.toString(),
                value = bar.value,
                color = if (currentDay.first() == bar.dayOfWeek) Info700 else Info400
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
fun SleepTitleCard() {
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
            text = "Patrón de sueño",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = Info500,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun SleepDataCard(text1: String, text2: String, porcentaje: Int) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(text = text1, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Info400)

            Column(modifier = Modifier.padding(top = 5.dp)) {
                Text(text = text2, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Info500)
                Text(text = "${porcentaje}% total", fontSize = 16.sp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun SleepDebtCard() {
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
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Deuda de sueño",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = Info400
            )

            Column(modifier = Modifier.padding(top = 5.dp)) {
                Text(
                    text = "-3h 12m",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Info500
                )
                Text(text = "Esta semana", fontSize = 16.sp, color = Color.LightGray)
            }
        }
    }
}