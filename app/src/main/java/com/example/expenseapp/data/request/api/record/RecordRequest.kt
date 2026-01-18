package com.example.expenseapp.data.request.api.record

import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class RecordRequest(
    val accountType: String,
    val amount: Int,
    val category: String,
    var date: String,
    val note: Note,
    val title: String
)

fun RecordRequest.toUtcDateString(localDate: LocalDate) {
    date = localDate.atStartOfDay(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT)
}
