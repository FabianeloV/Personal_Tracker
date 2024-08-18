package com.fevr.personaltracker.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.datastore.preferences.core.floatPreferencesKey
import com.fevr.personaltracker.DataStore
import com.fevr.personaltracker.ui.theme.Info400
import com.fevr.personaltracker.ui.theme.Info500
import com.fevr.personaltracker.ui.theme.Info700
import com.fevr.personaltracker.ui.theme.Primary400
import com.fevr.personaltracker.ui.theme.Primary500
import com.fevr.personaltracker.ui.theme.Primary700
import com.fevr.personaltracker.ui.theme.Purple40
import com.fevr.personaltracker.roomResources.Expense
import com.fevr.personaltracker.ui.theme.Warning500
import com.fevr.personaltracker.viewModels.MoneyTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyTrackerScreen(viewModel: MoneyTrackerViewModel = MoneyTrackerViewModel(Application())) {
    val context = LocalContext.current

    //Bottom sheet state y Income or expense state
    var showBottomSheet by remember { mutableStateOf(false) }
    val incomeOrExpenseState = remember { mutableStateOf(true) }

    //Creamos el datastore del current balance con una clave unica
    val balanceKey = floatPreferencesKey("balance_counter")
    val balanceCounter = DataStore(context).getBalance(balanceKey).collectAsState(initial = 0.0f)

    //Lista de gastos
    val db = viewModel.getDatabase(context)
    val expenses by db.expenseDao().getAllExpenses().observeAsState(initial = emptyList())

    //UI de aqui en adelante
    Surface(
        color = Primary400,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BalanceCard(balance = balanceCounter.value, 42, 20)

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(15.dp)
            ) {
                IncomeOrExpenseText(state = incomeOrExpenseState)

                IncomeOrExpenseSwitch(state = incomeOrExpenseState)
            }

        }
        Surface(
            modifier = Modifier.padding(top = 150.dp),
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            shadowElevation = 20.dp
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                if (incomeOrExpenseState.value) { } else {
                    expenses.forEach { expense ->
                        item { TransactionCard(expense, viewModel) }
                    }

                }
            }
        }
        AddTransactionButton { showBottomSheet = true }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            // Sheet content
            TransactionKeyboard(viewModel)
        }
    }
}

@Composable
fun IncomeOrExpenseText(state: MutableState<Boolean>) {
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
            text = if (state.value) "Ingresos" else "Egresos",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = if (state.value) Primary500 else Info400,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun IncomeOrExpenseSwitch(state: MutableState<Boolean>) {
    Switch(
        checked = state.value,
        onCheckedChange = { state.value = it },
        thumbContent = {
            if (state.value) {
                Text(text = "I", color = Color.White)
            } else {
                Text(text = "E", color = Color.White)
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Primary500,
            checkedTrackColor = Color.White,
            uncheckedThumbColor = Info400,
            uncheckedTrackColor = Color.White,
        )
    )
}

@Composable
fun BalanceCard(balance: Float, fontSize: Int, elevation: Int) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(elevation.dp),
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
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Black,
            color = if (balance < 0) Primary700 else Primary400,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun TransactionCard(expense: Expense, viewModel: MoneyTrackerViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 30.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Warning500,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = viewModel.getIcon(expense.type),
                contentDescription = expense.description
            )

            Text(text = expense.description)

            Text(text = "$ " + expense.value.toString())
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

@Composable
fun TransactionKeyboard(viewModel: MoneyTrackerViewModel) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.numbers.forEach { list ->
            NumberRow(numbers = list)
        }
    }
}

@Composable
fun NumberRow(numbers: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        numbers.forEach { number ->
            ElevatedButton(
                onClick = { },
                modifier = Modifier.size(60.dp)
            ) {
                Text(text = number, fontSize = 24.sp, color = Info500)
            }
        }
    }
}