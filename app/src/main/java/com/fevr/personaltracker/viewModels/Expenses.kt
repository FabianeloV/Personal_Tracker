package com.fevr.personaltracker.viewModels

data class Expenses(
    var type: ExpenseType,
    var description: String,
    var value: Float
)
