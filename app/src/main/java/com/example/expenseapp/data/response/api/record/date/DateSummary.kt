package com.example.expenseapp.data.response.api.record.date

import androidx.compose.runtime.mutableStateOf
import com.example.expenseapp.db.record.RecordEntity

data class DateSummary(
    val limit: String,
    val page: String,
    val records: List<Record>,
    val totalPages: Int,
    val totalRecords: Int
)

fun DateSummary.toEntity(): List<RecordEntity>{
    val recordEntity = mutableListOf<RecordEntity>()
    for(record in records){
        recordEntity.add(RecordEntity(
            recordId = record.id,
            accountTypeName = record.accountTypeName,
            amount = record.amount,
            categoryName = record.categoryName,
            isIncome = record.isIncome,
            title = record.title
        ))
    }
    return recordEntity
}