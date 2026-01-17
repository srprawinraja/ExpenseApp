package com.example.expenseapp.data.response.api.record

data class RecordResponse(
    val __v: Int,
    val _id: String,
    val accountType: String,
    val amount: Int,
    val category: String,
    val createdAt: String,
    val dateAndTime: String,
    val note: Note,
    val title: String,
    val updatedAt: String
)