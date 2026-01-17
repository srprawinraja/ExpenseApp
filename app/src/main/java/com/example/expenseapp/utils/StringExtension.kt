package com.example.expenseapp.utils

fun String.isInt(): Boolean {
    return this.toIntOrNull() != null
}
