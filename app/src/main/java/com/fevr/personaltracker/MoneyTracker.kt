package com.fevr.personaltracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fevr.personaltracker.ui.theme.Pink40
import com.fevr.personaltracker.ui.theme.Pink80
import com.fevr.personaltracker.ui.theme.Purple40

@Composable
fun MoneyTrackerScreen(navController: NavController) {
    var balance by remember { mutableIntStateOf(10) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BalanceCard(balance = balance)

        Button(onClick = { balance++ }) {
            Text(text = "subir")
        }
    }
}

@Composable
fun BalanceCard(balance: Int) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(10.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Red,
            disabledContainerColor = Color.White,
            disabledContentColor = Purple40
        )
    ) {
        Text(
            text = "$balance $",
            fontSize = 32.sp,
            color = Pink40,
            modifier = Modifier.padding(30.dp)
        )
    }
}