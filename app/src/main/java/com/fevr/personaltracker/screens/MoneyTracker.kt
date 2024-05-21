package com.fevr.personaltracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CheckCircle
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
import androidx.datastore.preferences.core.intPreferencesKey
import com.fevr.personaltracker.DataStore
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary700
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.ui.theme.Success500
import kotlinx.coroutines.launch

@Composable
fun MoneyTrackerScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Creamos el datastore del current balance con una clave unica
    val balanceKey = intPreferencesKey("balance_counter")
    val balanceCounter = DataStore(context).getBalance(balanceKey).collectAsState(initial = 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BalanceCard(balance = balanceCounter.value!!)

        TransactionCard()

        TransactionCard()

        TransactionCCard()

        Button(onClick = { scope.launch {DataStore(context).incrementCounter(balanceKey)} }) {
            Text(text = "subir")
        }

        Button(onClick = { scope.launch {DataStore(context).decrementCounter(balanceKey)} }) {
            Text(text = "bajar")
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
            color = if (balance<0) Primary700 else Primary400,
            modifier = Modifier.padding(30.dp)
        )
    }
}

@Composable
fun TransactionCard(){
    Card {
        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Test")

        Text(text = "Deposito")

        Text(text = "100", color = Success500)
    }
}

@Composable
fun TransactionCCard(){
    Card {
        Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "Test")

        Text(text = "Deposito")

        Text(text = "100", color = Primary700)
    }
}