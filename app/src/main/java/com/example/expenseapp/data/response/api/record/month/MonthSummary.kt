package com.example.expenseapp.data.response.api.record.month

data class MonthSummary(
    val dates: List<Date>,
    val expense: Int,
    val income: Int,
    val total: Int
)