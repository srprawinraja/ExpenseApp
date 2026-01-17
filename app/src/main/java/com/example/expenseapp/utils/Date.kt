package com.example.expenseapp.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun isValidDate(
    value: String,
    pattern: String = "dd-MM-yyyy"
): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDate.parse(value, formatter) // <-- LocalDate, not LocalDateTime
        true
    } catch (e: DateTimeParseException) {
        false
    }
}

