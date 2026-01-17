package com.example.expenseapp.data.request.api.record

data class RecordRequest(
    val accountType: String,
    val amount: Int,
    val category: String,
    val date: String,
    val note: Note,
    val title: String
)