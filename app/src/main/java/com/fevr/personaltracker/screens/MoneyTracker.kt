package com.fevr.personaltracker.screens

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import com.fevr.personaltracker.roomResources.MoneyTrackerDatabase
import com.fevr.personaltracker.roomResources.ExpenseType
import com.fevr.personaltracker.roomResources.Income
import com.fevr.personaltracker.ui.theme.Info600
import com.fevr.personaltracker.ui.theme.Success500
import com.fevr.personaltracker.ui.theme.Warning500
import com.fevr.personaltracker.viewModels.MoneyTrackerViewModel
import java.text.NumberFormat
import java.util.Locale

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
    val incomes by db.incomeDao().getAllExpenses().observeAsState(initial = emptyList())

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
                if (incomeOrExpenseState.value) {
                    incomes.forEach { income ->
                        item {
                            TransactionCard(
                                text = income.description,
                                value = income.value,
                                icon = Icons.Filled.KeyboardArrowUp,
                                color = Success500
                            )
                        }
                    }
                } else {
                    expenses.forEach { expense ->
                        item {
                            TransactionCard(
                                expense.description,
                                expense.value,
                                viewModel.getIcon(expense.type),
                                Warning500
                            )
                        }
                    }

                }
            }
        }
        AddTransactionButton { showBottomSheet = true }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
        ) {
            // Sheet content
            TransactionKeyboard(viewModel, db, context, incomeOrExpenseState) { showBottomSheet = false }
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
        ),
        modifier = Modifier.padding(top = 10.dp)
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
fun TransactionCard(text: String, value: Float, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 30.dp),
        shape = CircleShape,
        colors = CardColors(
            containerColor = Color.White,
            contentColor = color,
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
                imageVector = icon,
                contentDescription = null
            )

            Text(text = text)

            Text(
                text = NumberFormat.getCurrencyInstance(Locale.US)
                    .format(value)
            )
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
fun TransactionKeyboard(
    viewModel: MoneyTrackerViewModel,
    db: MoneyTrackerDatabase,
    context: Context,
    incomeOrExpense: MutableState<Boolean>,
    click: () -> Unit,
) {
    //Value of transaction
    val textFieldValue = remember { mutableStateOf("0") }

    //Type of expense
    val expenseType: MutableState<ExpenseType> = remember { mutableStateOf(ExpenseType.Ropa) }

    //Description of income
    val incomeDescription = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                shape = CircleShape,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            ) {
                Text(
                    text = if (textFieldValue.value.isNotEmpty()) NumberFormat.getCurrencyInstance(
                        Locale.US
                    )
                        .format(textFieldValue.value.toDoubleOrNull()) else "0",
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp,
                    color = Primary400,
                    modifier = Modifier.padding(10.dp)
                )
            }

            if (incomeOrExpense.value) {
                IncomeDescriptionField(description = incomeDescription)
            } else {
                ExpenseDropdownMenu(expense = expenseType, viewModel = viewModel)
            }
        }


        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column {
                viewModel.numbers.forEach { list ->
                    NumberRow(numbers = list, textFieldValue)
                }
                LastRow(mutableState = textFieldValue)
            }
            DeleteAndSubmit(
                mutableState = textFieldValue
            ) {
                if (incomeOrExpense.value) {
                    viewModel.insertIncome(
                        income = Income(
                            description = incomeDescription.value,
                            value = textFieldValue.value.toFloat()
                        ), db
                    )

                    viewModel.increaseTotal(textFieldValue.value.toFloat(), context)

                    click()

                } else {
                    viewModel.insertExpense(
                        expense = Expense(
                            type = expenseType.value.type,
                            description = expenseType.value.type,
                            value = textFieldValue.value.toFloat()
                        ), db
                    )

                    viewModel.decreaseTotal(textFieldValue.value.toFloat(), context)

                    click()
                }

            }
        }
    }
}

@Composable
fun NumberRow(numbers: List<String>, mutableState: MutableState<String>) {
    Row(
        modifier = Modifier.padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        numbers.forEach { number ->
            ElevatedButton(
                onClick = { mutableState.value += number },
                modifier = Modifier.size(80.dp)
            ) {
                Text(text = number, fontSize = 32.sp, color = Info500)
            }
        }
    }
}

@Composable
fun LastRow(mutableState: MutableState<String>) {
    Row(
        modifier = Modifier
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ElevatedButton(
            onClick = { mutableState.value = "0" },
            modifier = Modifier.size(80.dp)
        ) {
            Text(text = "C", fontSize = 32.sp, color = Info700)
        }

        ElevatedButton(
            onClick = { mutableState.value += "0" },
            modifier = Modifier.size(80.dp)
        ) {
            Text(text = "0", fontSize = 32.sp, color = Info500)
        }

        ElevatedButton(
            onClick = {
                if (!(mutableState.value.contains("."))) {
                    mutableState.value += "."
                }
            },
            modifier = Modifier.size(80.dp)
        ) {
            Text(text = ".", fontSize = 32.sp, color = Info700)
        }
    }
}

@Composable
fun DeleteAndSubmit(mutableState: MutableState<String>, click: () -> Unit) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        ElevatedButton(
            onClick = {
                if (mutableState.value.isNotEmpty()) {
                    mutableState.value = mutableState.value.dropLast(1) // Remove the last character
                }
            },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "delete",
                tint = Primary700
            )
        }

        Spacer(modifier = Modifier.padding(top = 90.dp))

        ElevatedButton(
            onClick = {
                if (mutableState.value.isNotEmpty() && mutableState.value != "0") {
                    click()
                }
            },
            modifier = Modifier
                .size(width = 80.dp, height = 160.dp)
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "delete", tint = Primary500)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropdownMenu(expense: MutableState<ExpenseType>, viewModel: MoneyTrackerViewModel) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
        OutlinedTextField(
            value = expense.value.type,
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Info400
            ),
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = expense.value.icon,
                    contentDescription = null,
                    tint = Info400
                )
            },
            modifier = Modifier
                .menuAnchor()
                .padding(start = 15.dp, end = 20.dp),
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Info400,
                unfocusedIndicatorColor = Info400,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            singleLine = true,
            maxLines = 1
        )
        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            viewModel.expensesType.forEach { item ->
                DropdownMenuItem(text = {
                    Row {
                        Icon(imageVector = item.icon, contentDescription = null, tint = Info400)

                        Text(
                            text = item.type,
                            color = Info500,
                            fontWeight = FontWeight.Black
                        )
                    }
                }, onClick = {
                    expense.value.type = item.type
                    expense.value.icon = item.icon
                    isExpanded = false
                })
            }
        }
    }
}

@Composable
fun IncomeDescriptionField(description: MutableState<String>) {
    OutlinedTextField(
        value = description.value,
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Info400
        ),
        onValueChange = { description.value = it },
        label = {
            Text(
                text = "Descripción"
            )
        },
        leadingIcon = { Icon(imageVector = Icons.Filled.Menu, contentDescription = null, tint = Info500)},
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Info400,
            unfocusedIndicatorColor = Info600,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        modifier = Modifier.padding(start = 15.dp, end = 20.dp),
        singleLine = true,
        maxLines = 2,
        minLines = 1,
    )
}