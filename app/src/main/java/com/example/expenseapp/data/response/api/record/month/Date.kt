package com.example.expenseapp.data.response.api.record.month

data class Date(
    val date: String,
    val dayName: String,
    val expense: Int,
    val income: Int,
    val total: Int
)