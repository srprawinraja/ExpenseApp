package com.example.expenseapp.db.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class RecordEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "record_id") val recordId: String,
    @ColumnInfo(name = "account_type_name") val accountTypeName: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "is_income") val isIncome: Boolean,
    @ColumnInfo(name = "title") val title: String,
)
