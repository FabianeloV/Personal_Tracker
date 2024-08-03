package com.fevr.personaltracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.floatPreferencesKey
import com.fevr.personaltracker.DataStore
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info700
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Primary700
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.ui.theme.Success500
import com.fevr.personaltracker.ui.theme.Warning500
import com.fevr.personaltracker.ui.theme.Warning600
import com.fevr.personaltracker.ui.theme.Warning700
import com.fevr.personaltracker.ui.theme.Warning800
import kotlinx.coroutines.launch

@Composable
fun MoneyTrackerScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Creamos el datastore del current balance con una clave unica
    val balanceKey = floatPreferencesKey("balance_counter")
    val balanceCounter = DataStore(context).getBalance(balanceKey).collectAsState(initial = 0.0f)

    Surface(
        color = Primary400,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(top = 20.dp, start = 20.dp)) {
            BalanceCard(balance = balanceCounter.value!!)
        }
        Surface(
            modifier = Modifier.padding(top = 150.dp),
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            shadowElevation = 20.dp
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                for (i in 0..16){
                    item { 
                        TransactionCard(state = i%2 == 0)
                    }
                }
            }

            AddTransactionButton { /*TODO*/ }
        }


        /*
        Button(onClick = { scope.launch { DataStore(context).incrementCounter(balanceKey, 2.55f) } }) {
            Text(text = "subir")
        }

        Button(onClick = { scope.launch { DataStore(context).decrementCounter(balanceKey, 1.35f) } }) {
            Text(text = "bajar")
        } */

    }
}

@Composable
fun BalanceCard(balance: Float) {
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
            text = "$%.02f".format(balance),
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = if (balance < 0) Primary700 else Primary400,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun TransactionCard(state: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 30.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = if (state) Success500 else Warning700,
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

@Composable
fun AddTransactionButton(click: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = { click() },
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(5.dp),
            containerColor = Info700,
            contentColor = Color.White,
            modifier = Modifier.padding(end = 35.dp, bottom = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add transaction"
            )
        }
    }
}