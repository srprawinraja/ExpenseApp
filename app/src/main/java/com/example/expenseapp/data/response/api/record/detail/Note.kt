package com.example.expenseapp.data.response.api.record.detail

data class Note(
    val content: String,
    val keys: List<String>,
    val urls: List<String>
)