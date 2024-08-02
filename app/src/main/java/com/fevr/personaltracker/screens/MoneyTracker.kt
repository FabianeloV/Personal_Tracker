package com.fevr.personaltracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.floatPreferencesKey
import com.fevr.personaltracker.DataStore
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Primary700
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.ui.theme.Success500
import kotlinx.coroutines.launch

@Composable
fun MoneyTrackerScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Creamos el datastore del current balance con una clave unica
    val balanceKey = floatPreferencesKey("balance_counter")
    val balanceCounter = DataStore(context).getBalance(balanceKey).collectAsState(initial = 0.0f)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BalanceCard(balance = balanceCounter.value!!)

        for (i in 1..3) {
            TransactionCard(state = i % 2 == 0)
        }

        Button(onClick = { scope.launch { DataStore(context).incrementCounter(balanceKey, 2.55f) } }) {
            Text(text = "subir")
        }

        Button(onClick = { scope.launch { DataStore(context).decrementCounter(balanceKey, 1.35f) } }) {
            Text(text = "bajar")
        }
    }
}

@Composable
fun BalanceCard(balance: Float) {
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
            text = "%.02f $".format(balance),
            fontSize = 32.sp,
            color = if (balance < 0) Primary700 else Primary400,
            modifier = Modifier.padding(30.dp)
        )
    }
}

@Composable
fun TransactionCard(state: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = if (state) Success500 else Primary500,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = if (state) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Test"
            )

            Text(text = "Deposito")

            Text(text = "100")
        }
    }
}