package com.example.expenseapp.data.response.api.record.date

data class Record(
    val id: String,
    val accountTypeName: String,
    val amount: Int,
    val categoryName: String,
    val isIncome: Boolean,
    val title: String
)