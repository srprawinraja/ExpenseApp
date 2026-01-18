package com.example.expenseapp.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

fun isValidDate(
    value: String,
    pattern: String = "yyyy-MM-dd"
): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDate.parse(value, formatter) // <-- LocalDate, not LocalDateTime
        true
    } catch (e: DateTimeParseException) {
        false
    }
}

fun getAsDate(
    value: String,
    pattern: String = "yyyy-MM-dd"
): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(value, formatter)
}


