package com.example.expenseapp.data.response.api.record.detail

data class RecordResponse(
    val __v: Int,
    val _id: String,
    val accountType: AccountType,
    val amount: Int,
    val category: Category,
    val createdAt: String,
    val date: String,
    val note: Note,
    val title: String,
    val updatedAt: String
)